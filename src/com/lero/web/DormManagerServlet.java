package com.lero.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lero.dao.DormManagerDao;
import com.lero.model.DormManager;
import com.lero.model.PageBean;
import com.lero.util.DbUtil;
import com.lero.util.PropertiesUtil;
import com.lero.util.StringUtil;

public class DormManagerServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	DormManagerDao dormManagerDao = new DormManagerDao();
	
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
		String s_dormManagerText = request.getParameter("s_dormManagerText");
		String searchType = request.getParameter("searchType");
		String page = request.getParameter("page");
		String action = request.getParameter("action");
		DormManager dormManager = new DormManager();
		if("preSave".equals(action)) {
			dormManagerPreSave(request, response);
			return;
		} else if("save".equals(action)){
			dormManagerSave(request, response);
			return;
		} else if("delete".equals(action)){
			dormManagerDelete(request, response);
			return;
		} else 
			if("list".equals(action)) {
			if(StringUtil.isNotEmpty(s_dormManagerText)) {
				if("name".equals(searchType)) {
					dormManager.setName(s_dormManagerText);
				} else if("userName".equals(searchType)) {
					dormManager.setUserName(s_dormManagerText);
				}
			}
			session.removeAttribute("s_dormManagerText");
			session.removeAttribute("searchType");
			request.setAttribute("s_dormManagerText", s_dormManagerText);
			request.setAttribute("searchType", searchType);
		} else if("search".equals(action)){
			if (StringUtil.isNotEmpty(s_dormManagerText)) {
				if ("name".equals(searchType)) {
					dormManager.setName(s_dormManagerText);
				} else if ("userName".equals(searchType)) {
					dormManager.setUserName(s_dormManagerText);
				}
				session.setAttribute("searchType", searchType);
				session.setAttribute("s_dormManagerText", s_dormManagerText);
			} else {
				session.removeAttribute("s_dormManagerText");
				session.removeAttribute("searchType");
			}
		} else {
			if(StringUtil.isNotEmpty(s_dormManagerText)) {
				if("name".equals(searchType)) {
					dormManager.setName(s_dormManagerText);
				} else if("userName".equals(searchType)) {
					dormManager.setUserName(s_dormManagerText);
				}
				session.setAttribute("searchType", searchType);
				session.setAttribute("s_dormManagerText", s_dormManagerText);
			}
			if(StringUtil.isEmpty(s_dormManagerText)) {
				Object o1 = session.getAttribute("s_dormManagerText");
				Object o2 = session.getAttribute("searchType");
				if(o1!=null) {
					if("name".equals((String)o2)) {
						dormManager.setName((String)o1);
					} else if("userName".equals((String)o2)) {
						dormManager.setUserName((String)o1);
					}
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
			List<DormManager> dormManagerList = dormManagerDao.dormManagerList(con, pageBean, dormManager);
			int total=dormManagerDao.dormManagerCount(con, dormManager);
			String pageCode = this.genPagation(total, Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			request.setAttribute("pageCode", pageCode);
			request.setAttribute("dormManagerList", dormManagerList);
			request.setAttribute("mainPage", "admin/dormManager.jsp");
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

	private void dormManagerDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String dormManagerId = request.getParameter("dormManagerId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			dormManagerDao.dormManagerDelete(con, dormManagerId);
			request.getRequestDispatcher("dormManager?action=list").forward(request, response);
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

	private void dormManagerSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String dormManagerId = request.getParameter("dormManagerId");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String tel = request.getParameter("tel");
		DormManager dormManager = new DormManager(userName, password, name, sex, tel);
		if(StringUtil.isNotEmpty(dormManagerId)) {
			dormManager.setDormManagerId(Integer.parseInt(dormManagerId));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			if(StringUtil.isNotEmpty(dormManagerId)) {
				saveNum = dormManagerDao.dormManagerUpdate(con, dormManager);
			} else if(dormManagerDao.haveManagerByUser(con, dormManager.getUserName())){
				request.setAttribute("dormManager", dormManager);
				request.setAttribute("error", "该用户名已存在");
				request.setAttribute("mainPage", "admin/dormManagerSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			} else {
				saveNum = dormManagerDao.dormManagerAdd(con, dormManager);
			}
			if(saveNum > 0) {
				request.getRequestDispatcher("dormManager?action=list").forward(request, response);
			} else {
				request.setAttribute("dormManager", dormManager);
				request.setAttribute("error", "保存失败");
				request.setAttribute("mainPage", "dormManager/dormManagerSave.jsp");
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

	private void dormManagerPreSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String dormManagerId = request.getParameter("dormManagerId");
		if(StringUtil.isNotEmpty(dormManagerId)) {
			Connection con = null;
			try {
				con = dbUtil.getCon();
				DormManager dormManager = dormManagerDao.dormManagerShow(con, dormManagerId);
				request.setAttribute("dormManager", dormManager);
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
		request.setAttribute("mainPage", "admin/dormManagerSave.jsp");
		request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
	}

	private String genPagation(int totalNum, int currentPage, int pageSize){
		int totalPage = totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<li><a href='dormManager?page=1'>首页</a></li>");
		if(currentPage==1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}else {
			pageCode.append("<li><a href='dormManager?page="+(currentPage-1)+"'>上一页</a></li>");
		}
		for(int i=currentPage-2;i<=currentPage+2;i++) {
			if(i<1||i>totalPage) {
				continue;
			}
			if(i==currentPage) {
				pageCode.append("<li class='active'><a href='#'>"+i+"</a></li>");
			} else {
				pageCode.append("<li><a href='dormManager?page="+i+"'>"+i+"</a></li>");
			}
		}
		if(currentPage==totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		} else {
			pageCode.append("<li><a href='dormManager?page="+(currentPage+1)+"'>下一页</a></li>");
		}
		pageCode.append("<li><a href='dormManager?page="+totalPage+"'>尾页</a></li>");
		return pageCode.toString();
	}
	
}
