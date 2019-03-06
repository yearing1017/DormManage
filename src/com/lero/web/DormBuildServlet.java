package com.lero.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lero.dao.DormBuildDao;
import com.lero.model.DormBuild;
import com.lero.model.DormManager;
import com.lero.model.PageBean;
import com.lero.util.DbUtil;
import com.lero.util.PropertiesUtil;
import com.lero.util.StringUtil;

public class DormBuildServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	DormBuildDao dormBuildDao = new DormBuildDao();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		String s_dormBuildName = request.getParameter("s_dormBuildName");
		String page = request.getParameter("page");
		String action = request.getParameter("action");
		DormBuild dormBuild = new DormBuild();
		if("preSave".equals(action)) {
			dormBuildPreSave(request, response);
			return;
		} else if("save".equals(action)){
			dormBuildSave(request, response);
			return;
		} else if("delete".equals(action)){
			dormBuildDelete(request, response);
			return;
		} else if("manager".equals(action)){
			dormBuildManager(request, response);
			return;
		} else if("addManager".equals(action)){
			dormBuildAddManager(request, response);
		} else if("move".equals(action)){
			managerMove(request, response);
		} else if("list".equals(action)) {
			if(StringUtil.isNotEmpty(s_dormBuildName)) {
				dormBuild.setDormBuildName(s_dormBuildName);
			}
			session.removeAttribute("s_dormBuildName");
			request.setAttribute("s_dormBuildName", s_dormBuildName);
		} else if("search".equals(action)){
			if(StringUtil.isNotEmpty(s_dormBuildName)) {
				dormBuild.setDormBuildName(s_dormBuildName);
				session.setAttribute("s_dormBuildName", s_dormBuildName);
			}else {
				session.removeAttribute("s_dormBuildName");
			}
		} else {
			if(StringUtil.isNotEmpty(s_dormBuildName)) {
				dormBuild.setDormBuildName(s_dormBuildName);
				session.setAttribute("s_dormBuildName", s_dormBuildName);
			}
			if(StringUtil.isEmpty(s_dormBuildName)) {
				Object o = session.getAttribute("s_dormBuildName");
				if(o!=null) {
					dormBuild.setDormBuildName((String)o);
				}
			}
		}
		if(StringUtil.isEmpty(page)) {
			page="1";
		}
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
		request.setAttribute("pageSize", pageBean.getPageSize());
		request.setAttribute("page", pageBean.getPage());
		try {
			con=dbUtil.getCon();
			List<DormBuild> dormBuildList = dormBuildDao.dormBuildList(con, pageBean, dormBuild);
			int total=dormBuildDao.dormBuildCount(con, dormBuild);
			String pageCode = this.genPagation(total, Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			request.setAttribute("pageCode", pageCode);
			request.setAttribute("dormBuildList", dormBuildList);
			request.setAttribute("mainPage", "admin/dormBuild.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void managerMove(HttpServletRequest request,
			HttpServletResponse response) {
		String dormBuildId = request.getParameter("dormBuildId");
		String dormManagerId = request.getParameter("dormManagerId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			dormBuildDao.managerUpdateWithId(con, dormManagerId, "0");
			request.getRequestDispatcher("dormBuild?action=manager&dormBuildId="+dormBuildId).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dormBuildAddManager(HttpServletRequest request,
			HttpServletResponse response) {
		String dormBuildId = request.getParameter("dormBuildId");
		String dormManagerId = request.getParameter("dormManagerId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			dormBuildDao.managerUpdateWithId(con, dormManagerId, dormBuildId);
			request.getRequestDispatcher("dormBuild?action=manager&dormBuildId="+dormBuildId).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dormBuildManager(HttpServletRequest request,
			HttpServletResponse response) {
		String dormBuildId = request.getParameter("dormBuildId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			List<DormManager> managerListWithId = dormBuildDao.dormManWithBuildId(con, dormBuildId);
			List<DormManager> managerListToSelect = dormBuildDao.dormManWithoutBuild(con);
			request.setAttribute("dormBuildId", dormBuildId);
			request.setAttribute("managerListWithId", managerListWithId);
			request.setAttribute("managerListToSelect", managerListToSelect);
			request.setAttribute("mainPage", "admin/selectManager.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dormBuildDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String dormBuildId = request.getParameter("dormBuildId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			if(dormBuildDao.existManOrDormWithId(con, dormBuildId)) {
				request.setAttribute("error", "宿舍楼下有宿舍或宿管，不能删除该宿舍楼");
			} else {
				dormBuildDao.dormBuildDelete(con, dormBuildId);
			}
			request.getRequestDispatcher("dormBuild?action=list").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void dormBuildSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String dormBuildId = request.getParameter("dormBuildId");
		String dormBuildName = request.getParameter("dormBuildName");
		String detail = request.getParameter("detail");
		DormBuild dormBuild = new DormBuild(dormBuildName, detail);
		if(StringUtil.isNotEmpty(dormBuildId)) {
			dormBuild.setDormBuildId(Integer.parseInt(dormBuildId));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			if(StringUtil.isNotEmpty(dormBuildId)) {
				saveNum = dormBuildDao.dormBuildUpdate(con, dormBuild);
			} else {
				saveNum = dormBuildDao.dormBuildAdd(con, dormBuild);
			}
			if(saveNum > 0) {
				request.getRequestDispatcher("dormBuild?action=list").forward(request, response);
			} else {
				request.setAttribute("dormBuild", dormBuild);
				request.setAttribute("error", "保存失败");
				request.setAttribute("mainPage", "dormBuild/dormBuildSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void dormBuildPreSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String dormBuildId = request.getParameter("dormBuildId");
		if(StringUtil.isNotEmpty(dormBuildId)) {
			Connection con = null;
			try {
				con = dbUtil.getCon();
				DormBuild dormBuild = dormBuildDao.dormBuildShow(con, dormBuildId);
				request.setAttribute("dormBuild", dormBuild);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} 
		request.setAttribute("mainPage", "admin/dormBuildSave.jsp");
		request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
	}

	private String genPagation(int totalNum, int currentPage, int pageSize){
		int totalPage = totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<li><a href='dormBuild?page=1'>首页</a></li>");
		if(currentPage==1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}else {
			pageCode.append("<li><a href='dormBuild?page="+(currentPage-1)+"'>上一页</a></li>");
		}
		for(int i=currentPage-2;i<=currentPage+2;i++) {
			if(i<1||i>totalPage) {
				continue;
			}
			if(i==currentPage) {
				pageCode.append("<li class='active'><a href='#'>"+i+"</a></li>");
			} else {
				pageCode.append("<li><a href='dormBuild?page="+i+"'>"+i+"</a></li>");
			}
		}
		if(currentPage==totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		} else {
			pageCode.append("<li><a href='dormBuild?page="+(currentPage+1)+"'>下一页</a></li>");
		}
		pageCode.append("<li><a href='dormBuild?page="+totalPage+"'>尾页</a></li>");
		return pageCode.toString();
	}
	
}
