package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Endereco;
import models.Pedido;
import models.NotaFiscal;
import models.User;

public class Request {
    private final String database = "db_poo";
    private final String user = "root";
    private final String password = "";
    private Connection connection;
    
    public Request() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+database+"?userSSL=false",user, password);
            connection.setAutoCommit(false);
        } catch(SQLException ex) {
            System.out.println("Erro ao tentar realizar conexão com o banco de dados");
            throw new RuntimeException(ex);
        }
    }
    
    
    public void adicionarEndereco(Endereco novoEndereco, int idUsuario) throws SQLException {
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO endereco(rua, cep, bairro, numero, id_usuario) VALUES (?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, novoEndereco.getRua());
            ps.setString(2, novoEndereco.getCep());
            ps.setString(3, novoEndereco.getBairro());
            ps.setInt(4, novoEndereco.getNumero());
            ps.setInt(5, idUsuario);
            ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("Erro ao tentar inserir no banco de dados");
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            connection.setAutoCommit(true);
        }
    }

    
    public List<Endereco> buscarPelaDescricao(String rua, int idUsuario) throws SQLException {
        String sql = "SELECT * FROM endereco WHERE rua LIKE ? AND id_usuario = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + rua + "%");
        ps.setInt(2, idUsuario);
        ResultSet rs = ps.executeQuery();
        List<Endereco> listaDeEnderecos = new ArrayList<>();
        while (rs.next()) {
            Endereco endereco = new Endereco(rs.getString("rua"),
                    rs.getString("cep"), rs.getString("bairro"), rs.getInt("numero"),
                    rs.getInt("id"));
            listaDeEnderecos.add(endereco);
        }
        rs.close();
        ps.close();
        return listaDeEnderecos;
    }
    
    public void removerEndereco(int id) throws SQLException {
        String sql = "DELETE FROM endereco WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

        try {
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            ps.close();
        }
    }
    
    public User login(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, senha);
        ResultSet rs =ps.executeQuery();
        User user = null;
        
        while (rs.next()) {
            user = new User();
            user.setId(rs.getInt("id"));
            user.setNome(rs.getString("nome"));
            user.setEmail(rs.getString("email"));
            user.setNivel(rs.getString("nivel"));

        }

        rs.close();
        ps.close();        
        return user;
    }
    
    public void closeConnection() throws SQLException {
        if(connection!=null){
           connection.close();
        }
    }
    
    public List<NotaFiscal> buscarNotasFiscais() throws SQLException {
        String sql = "SELECT * FROM nota_fiscal";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<NotaFiscal> listaDeNotasFiscais = new ArrayList<>();
        while (rs.next()) {
            NotaFiscal notaFiscal = new NotaFiscal(
                    rs.getInt("id_usuario"), rs.getInt("id_endereco"), rs.getBigDecimal("total"));
            listaDeNotasFiscais.add(notaFiscal);
        }
        rs.close();
        ps.close();
        return listaDeNotasFiscais;
    }

    public List<Pedido> buscarDetalhesPedido(int notaFiscalId) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE nota_fiscal_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, notaFiscalId);
        ResultSet rs = ps.executeQuery();
        List<Pedido> listaDePedidos = new ArrayList<>();
        while (rs.next()) {
            Pedido pedido = new Pedido(
                rs.getString("tamanho"),
                rs.getString("sabores"),
                rs.getString("bebidas"),
                rs.getString("borda"),
                rs.getBigDecimal("valor_total")
            );
            pedido.setId(rs.getInt("id"));
            pedido.setNotaFiscalId(rs.getInt("nota_fiscal_id"));
            listaDePedidos.add(pedido);
        }
        rs.close();
        ps.close();
        return listaDePedidos;
    }
    public void inserirNotaFiscal(NotaFiscal notaFiscal, List<Pedido> listaDePedidos) throws SQLException {
        PreparedStatement psNotaFiscal = null;
        PreparedStatement psPedido = null;

        try {

            // Inserir a nota fiscal
            String sqlNotaFiscal = "INSERT INTO nota_fiscal (id_usuario, id_endereco, total) VALUES (?, ?, ?)";
            psNotaFiscal = connection.prepareStatement(sqlNotaFiscal, Statement.RETURN_GENERATED_KEYS);
            psNotaFiscal.setInt(1, notaFiscal.getIdUsuario());
            psNotaFiscal.setInt(2, notaFiscal.getIdEndereco());
            psNotaFiscal.setBigDecimal(3, notaFiscal.getTotal());
            psNotaFiscal.executeUpdate();

            // Recuperar o id gerado pelo banco de dados
            ResultSet rs = psNotaFiscal.getGeneratedKeys();
            if (rs.next()) {
                int notaFiscalId = rs.getInt(1);

                // Inserir os detalhes do pedido
                String sqlPedido = "INSERT INTO pedido (nota_fiscal_id, tamanho, sabores, bebidas, borda, valor_total) VALUES (?, ?, ?, ?, ?, ?)";
                psPedido = connection.prepareStatement(sqlPedido);
                for (Pedido pedido : listaDePedidos) {
                    psPedido.setInt(1, notaFiscalId);
                    psPedido.setString(2, pedido.getTamanho());
                    psPedido.setString(3, pedido.getSabores());
                    psPedido.setString(4, pedido.getBebidas());
                    psPedido.setString(5, pedido.getBorda());
                    psPedido.setBigDecimal(6, pedido.getValorTotal());
                    psPedido.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (psNotaFiscal != null) {
                psNotaFiscal.close();
            }
            if (psPedido != null) {
                psPedido.close();
            }
        }
    } 
    
}
