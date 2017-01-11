<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>Insert title here</title>

</head>
<body>
       <aside>
        <div id="sidebar" class="nav-collapse">
          <ul class="sidebar-menu" id="nav-accordion">
            <li >
              <a href="index.html" class="active">
                <i class="fa fa-dashboard">
                </i>
                <span>
                  Dashboard
                </span>
              </a>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class="fa fa-laptop">
                </i>
                <span>
                  系统管理
                </span>
                <span class="label label-success span-sidebar">
                  5
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="../system/accountList">
                    用户管理
                  </a>
                </li>
                <li>
                  <a href="../system/roleList">
                    角色管理
                  </a>
                </li>
                <li>
                  <a href="../system/findAdminLogList">
                   系统日志
                  </a>
                </li>
                <li>
                  <a href="../system/findSysConfigureList">
                   系统参数配置
                  </a>
                </li>
                <li>
                  <a href="../system/accountIpList">
                    IP地址管理
                  </a>
                </li>
              </ul>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class="fa fa-book">
                </i>
                <span>
                  书籍管理
                </span>
                <span class="label label-info span-sidebar">
                  2
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="../book/temporaryBookList">
                    书籍管理
                  </a>
                </li>
                <li>
                  <a href="../book/jumpAddBook">
                    	新建书籍
                  </a>
                </li>
              </ul>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class="fa fa-cogs">
                </i>
                <span>
                  	作者管理
                </span>
                <span class="label label-primary span-sidebar">
                  1
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="../author/authorList">
                    	作者管理
                  </a>
                </li>
              </ul>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class="fa fa-tasks">
                </i>
                <span>
                  	请假（普通表单）
                </span>
                <span class="label label-info span-sidebar">
                  4
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="../leave/apply">
                    	请假申请（普通表单）
                  </a>
                </li>
                <li>
                  <a href="../leave/task">
                    	请假办理（普通表单）
                  </a>
                </li>
                <li>
                  <a href="../leave/running">
                   		运行中流程（普通表单）
                  </a>
                </li>
                <li>
                  <a href="../leave/finished">
                    	已结束流程（普通表单）
                  </a>
                </li>
              </ul>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class="fa fa-th">
                </i>
                <span>
                  	动态表单
                </span>
                <span class="label label-inverse span-sidebar">
                  4
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="../dynamic/process-list">
                    	流程列表（动态）
                  </a>
                </li>
                <li>
                  <a href="../dynamic/taskList">
                    	任务列表（动态）
                  </a>
                </li>
                <li>
                  <a href="../dynamic/runningList">
                    	运行中的流程表（动态）
                  </a>
                </li>
                <li>
                  <a href="../dynamic/finishedList">
                    	已结束流程表（动态）
                  </a>
                </li>
              </ul>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class=" fa fa-envelope">
                </i>
                <span>
                  	外置表单
                </span>
                <span class="label label-danger span-sidebar">
                  4
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="../formFormkey/process-list">
                    	流程列表（动态）
                  </a>
                </li>
                <li>
                  <a href="../formFormkey/taskList">
                    	任务列表（动态）
                  </a>
                </li>
                <li>
                  <a href="../formFormkey/runningList">
                    	运行中的流程表（动态）
                  </a>
                </li>
                <li>
                  <a href="../formFormkey/finishedList">
                    	已结束流程表（动态）
                  </a>
                </li>
              </ul>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class=" fa fa-bar-chart-o">
                </i>
                <span>
                  	流程管理
                </span>
                <span class="label label-warning span-sidebar">
                  3
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="../workflow/process-list">
                    	流程定义及部署管理
                  </a>
                </li>
                <li>
                  <a href="chartjs.html">
                    	运行中流程
                  </a>
                </li>
                <li>
                  <a href="flot_chart.html">
                    	模型工作区
                  </a>
                </li>
              </ul>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class="fa fa-shopping-cart">
                </i>
                <span>
                  管理模块
                </span>
                <span class="label label-success span-sidebar">
                  3
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="../management/engine">
                    引擎属性
                  </a>
                </li>
                <li>
                  <a href="../management/database">
                    引擎数据库
                  </a>
                </li>
                <li>
                  <a href="../management/jobList">
                    作业管理
                  </a>
                </li>
              </ul>
            </li>
            <li>
              <a href="google_maps.html">
                <i class="fa fa-map-marker">
                </i>
                <span>
                  Google Maps 
                </span>
              </a>
            </li>
            <li class="sub-menu">
              <a href="javascript:;" >
                <i class="fa fa-glass">
                </i>
                <span>
                  Extra Pages
                </span>
                <span class="label label-primary span-sidebar">
                  10
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="blank.html">
                    Blank Page
                  </a>
                </li>
                <li>
                  <a href="lock_screen.html">
                    Lock Screen
                  </a>
                </li>
                <li>
                  <a href="profile.html">
                    Profile
                  </a>
                </li>
                <li>
                  <a href="invoice.html">
                    Invoice
                  </a>
                </li>
                <li>
                  <a href="search_result.html">
                    Search Result
                  </a>
                </li>
                <li>
                  <a href="pricing_table.html">
                    Pricing Table
                  </a>
                </li>
                <li>
                  <a href="faq.html">
                    FAQ
                  </a>
                </li>
                <li class="active">
                  <a href="fb_wall.html">
                    Timeline
                  </a>
                </li>
                <li>
                  <a href="404.html">
                    404 Error
                  </a>
                </li>
                <li>
                  <a href="500.html">
                    500 Error
                  </a>
                </li>
              </ul>
            </li>
            <li>
              <a href="login.html">
                <i class="fa fa-user">
                </i>
                <span>
                  Login Page
                </span>
              </a>
            </li>
            <li class="sub-menu">
              <a href="javascript:;">
                <i class="fa fa-sitemap">
                </i>
                <span>
                  Multi level Menu
                </span>
              </a>
              <ul class="sub">
                <li>
                  <a href="javascript:;">
                    Menu Item 1
                  </a>
                </li>
                <li class="sub-menu">
                  <a href="boxed_page.html">
                    Menu Item 2 
                    <span class="label label-primary">
                      1
                    </span>
                  </a>
                  <ul class="sub">
                    <li>
                      <a href="javascript:;">
                        Item 2.1
                      </a>
                    </li>
                    <li class="sub-menu">
                      <a href="javascript:;">
                        Menu Item 3 
                        <span class="label label-primary">
                          3
                        </span>
                      </a>
                      <ul class="sub">
                        <li>
                          <a href="javascript:;">
                            Item 3.1
                          </a>
                        </li>
                        <li>
                          <a href="javascript:;">
                            Item 3.2
                          </a>
                        </li>
                        <li>
                          <a href="javascript:;">
                            Item 3.2
                          </a>
                        </li>
                      </ul>
                    </li>
                  </ul>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </aside>
     
</body>
</html>