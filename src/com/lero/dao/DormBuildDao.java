package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lero.model.DormBuild;
import com.lero.model.DormManager;
import com.lero.model.PageBean;
import com.lero.util.StringUtil;

public class DormBuildDao {

	public List<DormBuild> dormBuildList(Connection con, PageBean pageBean, DormBuild s_dormBuild)throws Exception {
		List<DormBuild> dormBuildList = new ArrayList<DormBuild>();
		StringBuffer sb = new StringBuffer("select * from t_dormBuild t1");
		if(StringUtil.isNotEmpty(s_dormBuild.getDormBuildName())) {
			sb.append(" where t1.dormBuildName like '%"+s_dormBuild.getDormBuildName()+"%'");
		}
		if(pageBean != null) {
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			DormBuild dormBuild=new DormBuild();
			dormBuild.setDormBuildId(rs.getInt("dormBuildId"));
			dormBuild.setDormBuildName(rs.getString("dormBuildName"));
			dormBuild.setDetail(rs.getString("dormBuildDetail"));
			dormBuildList.add(dormBuild);
		}
		return dormBuildList;
	}
	
	public static String dormBuildName(Connection con, int dormBuildId)throws Exception {
		String sql = "select * from t_dormBuild where dormBuildId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, dormBuildId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getString("dormBuildName");
		}
		return null;
	}
	
	public int dormBuildCount(Connection con, DormBuild s_dormBuild)throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) as total from t_dormBuild t1");
		if(StringUtil.isNotEmpty(s_dormBuild.getDormBuildName())) {
			sb.append(" where t1.dormBuildName like '%"+s_dormBuild.getDormBuildName()+"%'");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}
	
	public DormBuild dormBuildShow(Connection con, String dormBuildId)throws Exception {
		String sql = "select * from t_dormBuild t1 where t1.dormBuildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuildId);
		ResultSet rs=pstmt.executeQuery();
		DormBuild dormBuild = new DormBuild();
		if(rs.next()) {
			dormBuild.setDormBuildId(rs.getInt("dormBuildId"));
			dormBuild.setDormBuildName(rs.getString("dormBuildName"));
			dormBuild.setDetail(rs.getString("dormBuildDetail"));
		}
		return dormBuild;
	}
	
	public int dormBuildAdd(Connection con, DormBuild dormBuild)throws Exception {
		String sql = "insert into t_dormBuild values(null,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuild.getDormBuildName());
		pstmt.setString(2, dormBuild.getDetail());
		return pstmt.executeUpdate();
	}
	
	public int dormBuildDelete(Connection con, String dormBuildId)throws Exception {
		String sql = "delete from t_dormBuild where dormBuildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuildId);
		return pstmt.executeUpdate();
	}
	
	public int dormBuildUpdate(Connection con, DormBuild dormBuild)throws Exception {
		String sql = "update t_dormBuild set dormBuildName=?,dormBuildDetail=? where dormBuildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuild.getDormBuildName());
		pstmt.setString(2, dormBuild.getDetail());
		pstmt.setInt(3, dormBuild.getDormBuildId());
		return pstmt.executeUpdate();
	}
	
	public boolean existManOrDormWithId(Connection con, String dormBuildId)throws Exception {
		boolean isExist = false;
//		String sql="select * from t_dormBuild,t_dormManager,t_connection where dormManId=managerId and dormBuildId=buildId and dormBuildId=?";
		String sql = "select *from t_dormManager where dormBuildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuildId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			isExist = true;
		} else {
			isExist = false;
		}
		String sql1="select * from t_dormBuild t1,t_dorm t2 where t1.dormBuildId=t2.dormBuildId and t1.dormBuildId=?";
		PreparedStatement p=con.prepareStatement(sql1);
		p.setString(1, dormBuildId);
		ResultSet r = pstmt.executeQuery();
		if(r.next()) {
			return isExist;
		} else {
			return false;
		}
	}
	
	public List<DormManager> dormManWithoutBuild(Connection con)throws Exception {
		List<DormManager> dormManagerList = new ArrayList<DormManager>();
		String sql = "SELECT * FROM t_dormManager WHERE dormBuildId IS NULL OR dormBuildId=0";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			DormManager dormManager=new DormManager();
			dormManager.setDormBuildId(rs.getInt("dormBuildId"));
			dormManager.setDormManagerId(rs.getInt("dormManId"));
			dormManager.setName(rs.getString("name"));
			dormManager.setUserName(rs.getString("userName"));
			dormManager.setSex(rs.getString("sex"));
			dormManager.setTel(rs.getString("tel"));
			dormManagerList.add(dormManager);
		}
		return dormManagerList;
	}
	
	public List<DormManager> dormManWithBuildId(Connection con, String dormBuildId)throws Exception {
		List<DormManager> dormManagerList = new ArrayList<DormManager>();
		String sql = "select *from t_dormManager where dormBuildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuildId);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			DormManager dormManager=new DormManager();
			dormManager.setDormBuildId(rs.getInt("dormBuildId"));
			dormManager.setDormManagerId(rs.getInt("dormManId"));
			dormManager.setName(rs.getString("name"));
			dormManager.setUserName(rs.getString("userName"));
			dormManager.setSex(rs.getString("sex"));
			dormManager.setTel(rs.getString("tel"));
			dormManagerList.add(dormManager);
		}
		return dormManagerList;
	}
	
	public int managerUpdateWithId (Connection con, String dormManagerId, String dormBuildId)throws Exception {
		String sql = "update t_dormManager set dormBuildId=? where dormManId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuildId);
		pstmt.setString(2, dormManagerId);
		return pstmt.executeUpdate();
	}
}
