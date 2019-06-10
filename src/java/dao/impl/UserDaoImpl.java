/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import config.ConnectDB;
import dao.UserDao;
import dto.Rol;
import dto.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author carlr
 */
public class UserDaoImpl implements UserDao {

    private final StringBuilder sb;
    private final ConnectDB db;

    public UserDaoImpl() {
        this.sb = new StringBuilder();
        this.db = new ConnectDB();
    }

    @Override
    public List<User> getUsers() {
        sb.delete(0, sb.length()).append(""
                + "SELECT u.in_id, u.vc_username, r.in_id, r.vc_name "
                + "FROM db_app.t_user u "
                + "INNER JOIN db_app.t_rol r "
                + "ON r.in_id = u.in_id_rol "
                + "WHERE u.bl_enable = 1");

        try (Connection cn = db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sb.toString());
                ResultSet rs = ps.executeQuery()) {
            List<User> listUser = new LinkedList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("u.in_id"));
                user.setUsername(rs.getString("u.vc_username"));
                user.setPassword("????????");
                Rol rol = new Rol();
                rol.setId(rs.getInt("r.in_id"));
                rol.setName(rs.getString("r.vc_name"));
                user.setRol(rol);
                listUser.add(user);
            }
            return listUser;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public User getUser(int id) {
        sb.delete(0, sb.length()).append(""
                + "SELECT u.in_id, u.vc_username, r.in_id, r.vc_name "
                + "FROM db_app.t_user u "
                + "INNER JOIN db_app.t_rol r "
                + "ON r.in_id = u.in_id_rol "
                + "WHERE u.bl_enable = 1 AND u.in_id = ?");

        try (Connection cn = db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sb.toString());) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("u.in_id"));
                    user.setUsername(rs.getString("u.vc_username"));
                    user.setPassword("????????");
                    Rol rol = new Rol();
                    rol.setId(rs.getInt("r.in_id"));
                    rol.setName(rs.getString("r.vc_name"));
                    user.setRol(rol);
                    return user;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return null;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean createUser(User user) {
        sb.delete(0, sb.length()).append(""
                + "INSERT INTO db_app.t_user "
                + "(vc_username, vc_password, in_id_rol) "
                + "VALUES (?, ?, ?)");
        boolean ok = true;
        try (Connection cn = db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sb.toString());) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getRol().getId());
            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        sb.delete(0, sb.length()).append(""
                + "UPDATE db_app.t_user "
                + "SET vc_username = ?, vc_password = ?, in_id_rol = ? "
                + "WHERE in_id = ?");
        boolean ok = true;
        try (Connection cn = db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sb.toString());) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getRol().getId());
            ps.setInt(4, user.getId());
            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeUsers(ArrayList<Integer> ids) {
        sb.delete(0, sb.length()).append(""
                + "UPDATE db_app.t_user "
                + "SET bl_enable = 0 "
                + "WHERE in_id = ?");

        try (Connection cn = db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sb.toString());) {
            boolean ok = true;
            cn.setAutoCommit(false);
            for (Integer id : ids) {
                int ctos = ps.executeUpdate();
                if (ctos == 0) {
                    ok = false;
                }
            }
            if (ok) {
                cn.setAutoCommit(true);
                return true;
            } else {
                cn.rollback();
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

}
