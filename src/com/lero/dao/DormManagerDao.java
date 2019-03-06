package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lero.model.DormManager;
import com.lero.model.PageBean;
import com.lero.util.StringUtil;

public class DormManagerDao {

	public List<DormManager> dormManagerList(Connection con, PageBean pageBean, DormManager s_dormManager)throws Exception {
		List<DormManager> dormManagerList = new ArrayList<DormManager>();
		StringBuffer sb = new StringBuffer("SELECT * FROM t_dormManager t1");
		if(StringUtil.isNotEmpty(s_dormManager.getName())) {
			sb.append(" where t1.name like '%"+s_dormManager.getName()+"%' ORDER BY t1.userName");
		} else if(StringUtil.isNotEmpty(s_dormManager.getUserName())) {
			sb.append(" where t1.userName like '%"+s_dormManager.getUserName()+"%' ORDER BY t1.userName");
		}
		if(pageBean != null) {
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			DormManager dormManager=new DormManager();
			dormManager.setDormManagerId(rs.getInt("dormManId"));
			int dormBuildId = rs.getInt("dormBuildId");
			dormManager.setDormBuildId(dormBuildId);
			dormManager.setDormBuildName(DormBuildDao.dormBuildName(con, dormBuildId));
			dormManager.setName(rs.getString("name"));
			dormManager.setSex(rs.getString("sex"));
			dormManager.setUserName(rs.getString("userName"));
			dormManager.setTel(rs.getString("tel"));
			dormManager.setPassword(rs.getString("password"));
			dormManagerList.add(dormManager);
		}
		return dormManagerList;
	}
	
	public int dormManagerCount(Connection con, DormManager s_dormManager)throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) as total from t_dormManager t1");
		if(StringUtil.isNotEmpty(s_dormManager.getName())) {
			sb.append(" where t1.name like '%"+s_dormManager.getName()+"%'");
		} else if(StringUtil.isNotEmpty(s_dormManager.getUserName())) {
			sb.append(" where t1.userName like '%"+s_dormManager.getUserName()+"%'");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}
	
	public DormManager dormManagerShow(Connection con, String dormManagerId)throws Exception {
		String sql = "select * from t_dormManager t1 where t1.dormManId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormManagerId);
		ResultSet rs=pstmt.executeQuery();
		DormManager dormManager = new DormManager();
		if(rs.next()) {
			dormManager.setDormManagerId(rs.getInt("dormManId"));
			dormManager.setDormBuildId(rs.getInt("dormBuildId"));
			dormManager.setName(rs.getString("name"));
			dormManager.setSex(rs.getString("sex"));
			dormManager.setUserName(rs.getString("userName"));
			dormManager.setTel(rs.getString("tel"));
			dormManager.setPassword(rs.getString("password"));
		}
		return dormManager;
	}
	
	public int dormManagerAdd(Connection con, DormManager dormManager)throws Exception {
		String sql = "insert into t_dormManager values(null,?,?,null,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormManager.getUserName());
		pstmt.setString(2, dormManager.getPassword());
		pstmt.setString(3, dormManager.getName());
		pstmt.setString(4, dormManager.getSex());
		pstmt.setString(5, dormManager.getTel());
		return pstmt.executeUpdate();
	}
	
	public int dormManagerDelete(Connection con, String dormManagerId)throws Exception {
		String sql = "delete from t_dormManager where dormManId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormManagerId);
		return pstmt.executeUpdate();
	}
	
	public int dormManagerUpdate(Connection con, DormManager dormManager)throws Exception {
		String sql = "update t_dormManager set userName=?,password=?,name=?,sex=?,tel=? where dormManId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormManager.getUserName());
		pstmt.setString(2, dormManager.getPassword());
		pstmt.setString(3, dormManager.getName());
		pstmt.setString(4, dormManager.getSex());
		pstmt.setString(5, dormManager.getTel());
		pstmt.setInt(6, dormManager.getDormManagerId());
		return pstmt.executeUpdate();
	}

	public boolean haveManagerByUser(Connection con, String userName) throws Exception {
		String sql = "select * from t_dormmanager t1 where t1.userName=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, userName);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()) {
			return true;
		}
		return false;
	}
	
	
}
