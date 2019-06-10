/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import config.ConnectDB;
import dao.RolDao;
import dto.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author carlr
 */
public class RolDaoImpl implements RolDao{
    private final ConnectDB db;
    private final StringBuilder sb;

    public RolDaoImpl() {
        this.db = new ConnectDB();
        this.sb = new StringBuilder();
    }

    @Override
    public List<Rol> getRoles() {
        this.sb.delete(0, sb.length()).append(""
                + "SELECT in_id, vc_name FROM t_rol");
        try (Connection cn = db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sb.toString());
                ResultSet rs = ps.executeQuery()){            
            List<Rol> listRol = new LinkedList<>();            
            while (rs.next()) {                
                Rol rol = new Rol();
                rol.setId(rs.getInt("in_id"));
                rol.setName(rs.getString("vc_name"));
                listRol.add(rol);
            }
            return listRol;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
}
