webpackJsonp([1],{161:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.editProject=t.removeProject=t.addProject=t.getProjectList=t.getOption=t.removeTestReport=t.getTestReport=t.getTestReportList=t.addTestReport=t.requestLogin=void 0;var n=a(159),r=function(e){return e&&e.__esModule?e:{default:e}}(n);t.requestLogin=function(e){return r.default.post("/login",e).then(function(e){return e.data})},t.addTestReport=function(e){return r.default.post("/api/testReport",e)},t.getTestReportList=function(e){return r.default.get("/api/testReport/list/"+e)},t.getTestReport=function(e){return r.default.get("/api/testReport/"+e)},t.removeTestReport=function(e){return r.default.delete("/api/testReport/"+e)},t.getOption=function(e){return r.default.get("/api/testReport/options/"+e)},t.getProjectList=function(e){return r.default.get("/api/project",{params:e})},t.addProject=function(e){return r.default.post("/api/project",e,{headers:{"Content-Type":"application/json"}})},t.removeProject=function(e){return r.default.delete("/api/project/"+e)},t.editProject=function(e){return r.default.put("/api/project",e,{headers:{"Content-Type":"application/json"}})}},363:function(e,t,a){"use strict";function n(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var r=a(938),o=n(r),l=a(936),i=n(l),s=a(937),u=n(s),c=a(939),d=n(c),m=a(942),p=n(m),f=a(941),h=n(f),v=a(943),g=n(v),b=a(940),_=(n(b),[{path:"/login",component:o.default,name:"",hidden:!0},{path:"/404",component:i.default,name:"",hidden:!0},{path:"/",component:u.default,name:"项目管理",iconCls:"el-icon-message",children:[{path:"/main",component:d.default,name:"主页",hidden:!0},{path:"/table",component:p.default,name:"项目列表"},{path:"/form",component:h.default,name:"Form",hidden:!0},{path:"/user",component:g.default,name:"用例执行监控",hidden:!0}]},{path:"*",hidden:!0,redirect:{path:"/404"}}]);t.default=_},364:function(e,t,a){"use strict";function n(e){if(e&&e.__esModule)return e;var t={};if(null!=e)for(var a in e)Object.prototype.hasOwnProperty.call(e,a)&&(t[a]=e[a]);return t.default=e,t}function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var o=a(32),l=r(o),i=a(230),s=r(i),u=a(421),c=n(u),d=a(422),m=n(d);l.default.use(s.default);var p={count:10},f={INCREMENT:function(e){e.count++},DECREMENT:function(e){e.count--}};t.default=new s.default.Store({actions:c,getters:m,state:p,mutations:f})},367:function(e,t){},368:function(e,t){},369:function(e,t,a){a(933);var n=a(56)(a(411),a(952),null,null);e.exports=n.exports},411:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"app",components:{}}},412:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={data:function(){return{sysName:"用例执行监控",collapsed:!1,sysUserName:"",sysUserAvatar:"",form:{name:"",region:"",date1:"",date2:"",delivery:!1,type:[],resource:"",desc:""}}},methods:{onSubmit:function(){console.log("submit!")},handleopen:function(){},handleclose:function(){},handleselect:function(e,t){},logout:function(){var e=this;this.$confirm("确认退出吗?","提示",{}).then(function(){sessionStorage.removeItem("user"),e.$router.push("/login")}).catch(function(){})},collapse:function(){this.collapsed=!this.collapsed},showMenu:function(e,t){this.$refs.menuCollapsed.getElementsByClassName("submenu-hook-"+e)[0].style.display=t?"block":"none"}},mounted:function(){this.sysUserName="高燕丽",this.sysUserAvatar="https://raw.githubusercontent.com/taylorchen709/markdown-images/master/vueadmin/user.png"}}},413:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});a(161);t.default={data:function(){return{logining:!1,ruleForm2:{account:"admin",checkPass:"123456"},rules2:{account:[{required:!0,message:"请输入账号",trigger:"blur"}],checkPass:[{required:!0,message:"请输入密码",trigger:"blur"}]},checked:!0}},methods:{handleReset2:function(){this.$refs.ruleForm2.resetFields()},handleSubmit2:function(e){var t=this;this.$refs.ruleForm2.validate(function(e){if(!e)return console.log("error submit!!"),!1;t.logining=!0;t.ruleForm2.account,t.ruleForm2.checkPass;t.logining=!1,t.$router.push({path:"/table"})})}}}},414:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={}},415:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a(656),r=function(e){return e&&e.__esModule?e:{default:e}}(n);t.default={data:function(){return{chartColumn:null,chartBar:null,chartLine:null,chartPie:null}},methods:{drawColumnChart:function(){this.chartColumn=r.default.init(document.getElementById("chartColumn")),this.chartColumn.setOption({title:{text:"Column Chart"},tooltip:{},xAxis:{data:["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]},yAxis:{},series:[{name:"销量",type:"bar",data:[5,20,36,10,10,20]}]})},drawBarChart:function(){this.chartBar=r.default.init(document.getElementById("chartBar")),this.chartBar.setOption({title:{text:"Bar Chart",subtext:"数据来自网络"},tooltip:{trigger:"axis",axisPointer:{type:"shadow"}},legend:{data:["2011年","2012年"]},grid:{left:"3%",right:"4%",bottom:"3%",containLabel:!0},xAxis:{type:"value",boundaryGap:[0,.01]},yAxis:{type:"category",data:["巴西","印尼","美国","印度","中国","世界人口(万)"]},series:[{name:"2011年",type:"bar",data:[18203,23489,29034,104970,131744,630230]},{name:"2012年",type:"bar",data:[19325,23438,31e3,121594,134141,681807]}]})},drawLineChart:function(){this.chartLine=r.default.init(document.getElementById("chartLine")),this.chartLine.setOption({title:{text:"Line Chart"},tooltip:{trigger:"axis"},legend:{data:["邮件营销","联盟广告","搜索引擎"]},grid:{left:"3%",right:"4%",bottom:"3%",containLabel:!0},xAxis:{type:"category",boundaryGap:!1,data:["周一","周二","周三","周四","周五","周六","周日"]},yAxis:{type:"value"},series:[{name:"邮件营销",type:"line",stack:"总量",data:[120,132,101,134,90,230,210]},{name:"联盟广告",type:"line",stack:"总量",data:[220,182,191,234,290,330,310]},{name:"搜索引擎",type:"line",stack:"总量",data:[820,932,901,934,1290,1330,1320]}]})},drawPieChart:function(){this.chartPie=r.default.init(document.getElementById("chartPie")),this.chartPie.setOption({title:{text:"Pie Chart",subtext:"纯属虚构",x:"center"},tooltip:{trigger:"item",formatter:"{a} <br/>{b} : {c} ({d}%)"},legend:{orient:"vertical",left:"left",data:["直接访问","邮件营销","联盟广告","视频广告","搜索引擎"]},series:[{name:"访问来源",type:"pie",radius:"55%",center:["50%","60%"],data:[{value:335,name:"直接访问"},{value:310,name:"邮件营销"},{value:234,name:"联盟广告"},{value:135,name:"视频广告"},{value:1548,name:"搜索引擎"}],itemStyle:{emphasis:{shadowBlur:10,shadowOffsetX:0,shadowColor:"rgba(0, 0, 0, 0.5)"}}}]})},drawCharts:function(){this.drawColumnChart(),this.drawBarChart(),this.drawLineChart(),this.drawPieChart()}},mounted:function(){this.drawCharts()},updated:function(){this.drawCharts()}}},416:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={data:function(){return{form:{name:"",region:"",date1:"",date2:"",delivery:!1,type:[],resource:"",desc:""}}},methods:{onSubmit:function(){console.log("submit!")}}}},417:function(e,t,a){"use strict";function n(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var r=a(162),o=n(r),l=a(419),i=n(l),s=a(159),u=(n(s),a(161));t.default={data:function(){return{filters:{name:""},projectList:[],total:0,page:1,listLoading:!1,sels:[],editFormVisible:!1,editLoading:!1,editFormRules:{name:[{required:!0,message:"请输入项目名称",trigger:"blur"}],svnUrl:[{required:!0,message:"请输入SVN地址",trigger:"blur"}],round:[{required:!0,message:"请选择测试轮次",trigger:"blur"}]},editForm:{id:0,name:"",round:"",startDate:"",finishDate:""},addFormVisible:!1,addLoading:!1,addFormRules:{name:[{required:!0,message:"请输入项目名称",trigger:"blur"}],svnUrl:[{required:!0,message:"请输入SVN地址",trigger:"blur"}],round:[{required:!0,message:"请选择测试轮次",trigger:"blur"}]},addForm:{name:"",round:"",startDate:"",finishDate:""}}},methods:{translate:function(e,t){switch(e[t.property]){case"round1":return"第一轮";case"round2":return"第二轮";case"round3":return"第三轮";case"round4":return"第四轮";case"round5":return"第五轮";case"round6":return"第六轮";case"round7":return"第七轮";case"round8":return"第八轮"}},dateFormat:function(e,t){var a=e[t.property];return void 0==a?"":i.default.formatDate.format(new Date(a),"yyyy-MM-dd")},handleCurrentChange:function(e){this.page=e,this.getProjectList()},getProjectList:function(){var e=this,t={page:this.page,name:this.filters.name};this.listLoading=!0,(0,u.getProjectList)(t).then(function(t){console.log(t),e.total=t.data.total,e.projectList=t.data.projectList,e.listLoading=!1})},goDetails:function(e,t){this.$router.push({path:"/user",query:{id:t.id}})},handleDel:function(e,t){var a=this;this.$confirm("确认删除该记录吗?","提示",{type:"warning"}).then(function(){a.listLoading=!0;var e=t.id;(0,u.removeProject)(e).then(function(e){a.listLoading=!1,a.$message({message:"删除成功",type:"success"}),a.getProjectList()})}).catch(function(){})},handleEdit:function(e,t){this.editFormVisible=!0,this.editForm=(0,o.default)({},t)},handleAdd:function(){this.addFormVisible=!0,this.addForm={name:"",svnUrl:"",round:"",startDate:"",finishDate:""}},editSubmit:function(){var e=this;this.$refs.editForm.validate(function(t){t&&e.$confirm("确认提交吗？","提示",{}).then(function(){e.editLoading=!0;var t=(0,o.default)({},e.editForm);t.startDate=t.startDate&&""!=t.startDate?i.default.formatDate.format(new Date(t.startDate),"yyyy-MM-dd"):"",t.finishDate=t.finishDate&&""!=t.finishDate?i.default.formatDate.format(new Date(t.finishDate),"yyyy-MM-dd"):"",(0,u.editProject)(t).then(function(t){e.editLoading=!1,e.$message({message:"提交成功",type:"success"}),e.$refs.editForm.resetFields(),e.editFormVisible=!1,e.getProjectList()})})})},addSubmit:function(){var e=this;this.$refs.addForm.validate(function(t){t&&e.$confirm("确认提交吗？","提示",{}).then(function(){e.addLoading=!0;var t=(0,o.default)({},e.addForm);t.startDate=t.startDate&&""!=t.startDate?i.default.formatDate.format(new Date(t.startDate),"yyyy-MM-dd"):"",t.finishDate=t.finishDate&&""!=t.finishDate?i.default.formatDate.format(new Date(t.finishDate),"yyyy-MM-dd"):"",(0,u.addProject)(t).then(function(t){console.log(t),e.addLoading=!1,t.status&&200==t.status&&t.data.success?(e.$message({message:"提交成功",type:"success"}),e.$refs.addForm.resetFields(),e.addFormVisible=!1,e.getProjectList()):e.$alert(t.data.message,{})})})})},selsChange:function(e){this.sels=e},batchRemove:function(){var e=this,t=this.sels.map(function(e){return e.id}).toString();this.$confirm("确认删除选中记录吗？","提示",{type:"warning"}).then(function(){e.listLoading=!0;var a={ids:t};batchRemoveUser(a).then(function(t){e.listLoading=!1,e.$message({message:"删除成功",type:"success"}),e.getProjectList()})}).catch(function(){})}},mounted:function(){this.getProjectList()}}},418:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a(162),r=function(e){return e&&e.__esModule?e:{default:e}}(n),o=a(161);t.default={data:function(){return{filters:{name:""},options:{},optionsForPackage:[],optionsForModule:"",optionsForPriority:["0","1","2","3"],loading:!1,reports:[],addFormVisible:!1,addLoading:!1,addForm:{usecasePackage:"",module:"",executor:"",priorities:[]},loaded:!1}},methods:{selectPackage:function(e){console.log(e),console.log(this.options),this.optionsForModule=this.options[e],console.log(this.optionsForModule)},handleAdd:function(){this.addFormVisible=!0,this.getOptions(),this.addForm={usecasePackage:"",module:"",executor:"",priorities:[],projectId:this.$route.query.id}},addSubmit:function(){var e=this;this.$refs.addForm.validate(function(t){t&&e.$confirm("确认提交吗？","提示",{}).then(function(){e.addLoading=!0;var t=(0,r.default)({},e.addForm);console.log(t),t.priorities=e.addForm.priorities.join(","),console.log(t),(0,o.addTestReport)(t).then(function(t){e.addLoading=!1,e.$message({message:"提交成功",type:"success"}),e.$refs.addForm.resetFields(),e.addFormVisible=!1,e.getTestReport()})})})},refresh:function(e,t){var a=this;this.listLoading=!0;var n=t.id;(0,o.getTestReport)(n).then(function(e){a.listLoading=!1,a.$message({message:"刷新成功",type:"success"}),a.getTestReport()})},handleDel:function(e,t){var a=this;this.$confirm("确认删除该记录吗?","提示",{type:"warning"}).then(function(){a.listLoading=!0;var e=t.id;(0,o.removeTestReport)(e).then(function(e){a.listLoading=!1,a.$message({message:"删除成功",type:"success"}),a.getTestReport()})}).catch(function(){})},getOptions:function(){var e=this,t=this.$route.query.id;this.loaded=!0,(0,o.getOption)(t).then(function(t){console.log(t),e.loaded=!1,e.options=t.data.options,e.optionsForPackage=e.options.packages})},getTestReport:function(){var e=this;console.log(this.$route.query.id);var t=this.$route.query.id;this.loading=!0,(0,o.getTestReportList)(t).then(function(t){console.log(t),e.reports=t.data.reports,e.loading=!1})}},mounted:function(){this.getTestReport()}}},419:function(e,t,a){"use strict";function n(e,t){for(var t=t-(e+"").length,a=0;a<t;a++)e="0"+e;return e}Object.defineProperty(t,"__esModule",{value:!0});var r=/([yMdhsm])(\1*)/g;t.default={getQueryStringByName:function(e){var t=new RegExp("(^|&)"+e+"=([^&]*)(&|$)","i"),a=window.location.search.substr(1).match(t),n="";return null!=a&&(n=a[2]),t=null,a=null,null==n||""==n||"undefined"==n?"":n},formatDate:{format:function(e,t){return t=t||"yyyy-MM-dd",t.replace(r,function(t){switch(t.charAt(0)){case"y":return n(e.getFullYear(),t.length);case"M":return n(e.getMonth()+1,t.length);case"d":return n(e.getDate(),t.length);case"w":return e.getDay()+1;case"h":return n(e.getHours(),t.length);case"m":return n(e.getMinutes(),t.length);case"s":return n(e.getSeconds(),t.length)}})},parse:function(e,t){var a=t.match(r),n=e.match(/(\d)+/g);if(a.length==n.length){for(var o=new Date(1970,0,1),l=0;l<a.length;l++){var i=parseInt(n[l]);switch(a[l].charAt(0)){case"y":o.setFullYear(i);break;case"M":o.setMonth(i-1);break;case"d":o.setDate(i);break;case"h":o.setHours(i);break;case"m":o.setMinutes(i);break;case"s":o.setSeconds(i)}}return o}return null}}}},420:function(e,t,a){"use strict";function n(e){return e&&e.__esModule?e:{default:e}}var r=a(365),o=(n(r),a(32)),l=n(o),i=a(369),s=n(i),u=a(366),c=n(u);a(367);var d=a(370),m=n(d),p=a(364),f=n(p),h=a(230),v=n(h),g=a(159),b=(n(g),a(363)),_=n(b);a(368),l.default.use(c.default),l.default.use(m.default),l.default.use(v.default);var y=new m.default({routes:_.default});y.beforeEach(function(e,t,a){a()}),new l.default({router:y,store:f.default,render:function(e){return e(s.default)}}).$mount("#app")},421:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});t.increment=function(e){(0,e.commit)("INCREMENT")},t.decrement=function(e){(0,e.commit)("DECREMENT")}},422:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});t.getCount=function(e){return e.count}},926:function(e,t){},927:function(e,t){},928:function(e,t){},929:function(e,t){},930:function(e,t){},931:function(e,t){},932:function(e,t){},933:function(e,t){},936:function(e,t,a){a(926);var n=a(56)(null,a(944),"data-v-0f02ba32",null);e.exports=n.exports},937:function(e,t,a){a(928);var n=a(56)(a(412),a(947),"data-v-57bf35f5",null);e.exports=n.exports},938:function(e,t,a){a(931);var n=a(56)(a(413),a(950),"data-v-83e7217a",null);e.exports=n.exports},939:function(e,t,a){a(930);var n=a(56)(a(414),a(949),"data-v-743949cf",null);e.exports=n.exports},940:function(e,t,a){a(932);var n=a(56)(a(415),a(951),"data-v-ce436f94",null);e.exports=n.exports},941:function(e,t,a){var n=a(56)(a(416),a(946),null,null);e.exports=n.exports},942:function(e,t,a){a(929);var n=a(56)(a(417),a(948),"data-v-5e801943",null);e.exports=n.exports},943:function(e,t,a){a(927);var n=a(56)(a(418),a(945),"data-v-1c5c6b46",null);e.exports=n.exports},944:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement;return(e._self._c||t)("p",{staticClass:"page-container"},[e._v("404 page not found")])},staticRenderFns:[]}},945:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("section",[a("el-col",{staticClass:"toolbar",staticStyle:{"padding-bottom":"0px"},attrs:{span:24}},[a("el-form",{attrs:{inline:!0,model:e.filters}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"用例名称"},model:{value:e.filters.name,callback:function(t){e.$set(e.filters,"name",t)},expression:"filters.name"}})],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary"},on:{click:e.getTestReport}},[e._v("查询")])],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary"},on:{click:e.handleAdd}},[e._v("新增")])],1)],1)],1),e._v(" "),[a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%","min-height":"500px"},attrs:{data:e.reports,"highlight-current-row":"",fit:""}},[a("el-table-column",{attrs:{type:"index",width:"60"}}),e._v(" "),a("el-table-column",{attrs:{prop:"usecasePackage",label:"用例包名称",width:"140",sortable:""}}),e._v(" "),a("el-table-column",{attrs:{prop:"module",label:"功能模块",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{prop:"executor",label:"执行人",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{prop:"total",label:"用例总数",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{prop:"priorities",label:"用例等级",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{prop:"pass",label:"PASS数",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{prop:"fail",label:"FAIL数",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{prop:"finishRate",label:"完成进度(%)","min-width":"150"},scopedSlots:e._u([{key:"default",fn:function(e){return[a("el-progress",{attrs:{percentage:e.row.finishRate,"show-text":!0}})]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"操作","min-width":"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(a){e.refresh(t.$index,t.row)}}},[e._v("刷新")]),e._v(" "),a("el-button",{attrs:{type:"danger",size:"small"},on:{click:function(a){e.handleDel(t.$index,t.row)}}},[e._v("删除")])]}}])})],1)],e._v(" "),a("el-dialog",{attrs:{title:"新增","close-on-click-modal":!1},model:{value:e.addFormVisible,callback:function(t){e.addFormVisible=t},expression:"addFormVisible"}},[a("el-form",{ref:"addForm",staticStyle:{margin:"20px",width:"60%","min-width":"600px"},attrs:{model:e.addForm,"label-width":"80px"},on:{submit:function(t){return t.preventDefault(),e.onSubmit(t)}}},[a("el-form-item",{attrs:{label:"用例包"}},[a("el-select",{attrs:{placeholder:"请选择用例包",loading:e.loaded,"loading-text":"数据加载中"},on:{change:e.selectPackage},model:{value:e.addForm.usecasePackage,callback:function(t){e.$set(e.addForm,"usecasePackage",t)},expression:"addForm.usecasePackage"}},e._l(e.optionsForPackage,function(e){return a("el-option",{key:e.id,attrs:{label:e.value,value:e.value}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"功能模块"}},[a("el-select",{attrs:{placeholder:"请选择功能模块"},model:{value:e.addForm.module,callback:function(t){e.$set(e.addForm,"module",t)},expression:"addForm.module"}},e._l(e.optionsForModule,function(e){return a("el-option",{key:e.id,attrs:{label:e.value,value:e.value}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"执行人"}},[a("el-col",{attrs:{span:10}},[a("el-input",{attrs:{width:"10px"},model:{value:e.addForm.executor,callback:function(t){e.$set(e.addForm,"executor",t)},expression:"addForm.executor"}})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"用例等级"}},[a("el-checkbox-group",{model:{value:e.addForm.priorities,callback:function(t){e.$set(e.addForm,"priorities",t)},expression:"addForm.priorities"}},e._l(e.optionsForPriority,function(t){return a("el-checkbox",{key:t,attrs:{label:t}},[e._v(e._s(t))])}))],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",loading:e.addLoading},nativeOn:{click:function(t){return e.addSubmit(t)}}},[e._v("立即创建")]),e._v(" "),a("el-button",{on:{click:function(t){e.addFormVisible=!1}}},[e._v("取消")])],1)],1)],1)],2)},staticRenderFns:[]}},946:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-form",{ref:"form",staticStyle:{margin:"20px",width:"60%","min-width":"600px"},attrs:{model:e.form,"label-width":"80px"},on:{submit:function(t){return t.preventDefault(),e.onSubmit(t)}}},[a("el-form-item",{attrs:{label:"用例集合"}},[a("el-select",{attrs:{placeholder:"请选择用例集合"},model:{value:e.form.region,callback:function(t){e.$set(e.form,"region",t)},expression:"form.region"}},[a("el-option",{attrs:{label:"区域一",value:"shanghai"}}),e._v(" "),a("el-option",{attrs:{label:"区域二",value:"beijing"}})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"用例分页"}},[a("el-select",{attrs:{placeholder:"请选择用例分页"},model:{value:e.form.region,callback:function(t){e.$set(e.form,"region",t)},expression:"form.region"}},[a("el-option",{attrs:{label:"区域一",value:"shanghai"}}),e._v(" "),a("el-option",{attrs:{label:"区域二",value:"beijing"}})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"执行人"}},[a("el-input",{model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"用例等级"}},[a("el-checkbox-group",{model:{value:e.form.type,callback:function(t){e.$set(e.form,"type",t)},expression:"form.type"}},[a("el-checkbox",{attrs:{label:"L1",name:"type"}}),e._v(" "),a("el-checkbox",{attrs:{label:"L2",name:"type"}}),e._v(" "),a("el-checkbox",{attrs:{label:"L3",name:"type"}}),e._v(" "),a("el-checkbox",{attrs:{label:"L4",name:"type"}})],1)],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary"}},[e._v("立即创建")]),e._v(" "),a("el-button",{nativeOn:{click:function(e){e.preventDefault()}}},[e._v("取消")])],1)],1)},staticRenderFns:[]}},947:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-row",{staticClass:"container"},[a("el-col",{staticClass:"header",attrs:{span:24}},[a("el-col",{staticClass:"logo",class:e.collapsed?"logo-collapse-width":"logo-width",attrs:{span:10}},[e._v("\n\t\t\t"+e._s(e.collapsed?"":e.sysName)+"\n\t\t")]),e._v(" "),a("el-col",{staticClass:"userinfo",attrs:{span:4}},[a("el-dropdown",{attrs:{trigger:"hover"}},[a("span",{staticClass:"el-dropdown-link userinfo-inner"},[a("img",{attrs:{src:this.sysUserAvatar}}),e._v(" "+e._s(e.sysUserName))]),e._v(" "),a("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[a("el-dropdown-item",[e._v("我的消息")]),e._v(" "),a("el-dropdown-item",[e._v("设置")]),e._v(" "),a("el-dropdown-item",{attrs:{divided:""},nativeOn:{click:function(t){return e.logout(t)}}},[e._v("退出登录")])],1)],1)],1)],1),e._v(" "),a("el-col",{staticClass:"main",attrs:{span:24}},[a("aside",{class:e.collapsed?"menu-collapsed":"menu-expanded"},[a("el-menu",{directives:[{name:"show",rawName:"v-show",value:!e.collapsed,expression:"!collapsed"}],staticClass:"el-menu-vertical-demo",attrs:{"default-active":e.$route.path,"unique-opened":"",router:""},on:{open:e.handleopen,close:e.handleclose,select:e.handleselect}},[e._l(e.$router.options.routes,function(t,n){return t.hidden?e._e():[t.leaf?e._e():a("el-submenu",{attrs:{index:n+""}},[a("template",{slot:"title"},[a("i",{class:t.iconCls}),e._v(e._s(t.name))]),e._v(" "),e._l(t.children,function(t){return t.hidden?e._e():a("el-menu-item",{key:t.path,attrs:{index:t.path}},[e._v(e._s(t.name))])})],2),e._v(" "),t.leaf&&t.children.length>0?a("el-menu-item",{attrs:{index:t.children[0].path}},[a("i",{class:t.iconCls}),e._v(e._s(t.children[0].name))]):e._e()]})],2),e._v(" "),a("ul",{directives:[{name:"show",rawName:"v-show",value:e.collapsed,expression:"collapsed"}],ref:"menuCollapsed",staticClass:"el-menu el-menu-vertical-demo collapsed"},e._l(e.$router.options.routes,function(t,n){return t.hidden?e._e():a("li",{staticClass:"el-submenu item"},[t.leaf?[a("li",{staticClass:"el-submenu"},[a("div",{staticClass:"el-submenu__title el-menu-item",class:e.$route.path==t.children[0].path?"is-active":"",staticStyle:{"padding-left":"20px",height:"56px","line-height":"56px",padding:"0 20px"},on:{click:function(a){e.$router.push(t.children[0].path)}}},[a("i",{class:t.iconCls})])])]:[a("div",{staticClass:"el-submenu__title",staticStyle:{"padding-left":"20px"},on:{mouseover:function(t){e.showMenu(n,!0)},mouseout:function(t){e.showMenu(n,!1)}}},[a("i",{class:t.iconCls})]),e._v(" "),a("ul",{staticClass:"el-menu submenu",class:"submenu-hook-"+n,on:{mouseover:function(t){e.showMenu(n,!0)},mouseout:function(t){e.showMenu(n,!1)}}},e._l(t.children,function(t){return t.hidden?e._e():a("li",{key:t.path,staticClass:"el-menu-item",class:e.$route.path==t.path?"is-active":"",staticStyle:{"padding-left":"40px"},on:{click:function(a){e.$router.push(t.path)}}},[e._v(e._s(t.name))])}))]],2)}))],1),e._v(" "),a("section",{staticClass:"content-container"},[a("div",{staticClass:"grid-content bg-purple-light"},[a("el-col",{staticClass:"breadcrumb-container",attrs:{span:24}},[a("strong",{staticClass:"title"},[e._v(e._s(e.$route.name))]),e._v(" "),a("el-breadcrumb",{staticClass:"breadcrumb-inner",attrs:{separator:"/"}},e._l(e.$route.matched,function(t){return a("el-breadcrumb-item",{key:t.path},[e._v("\n\t\t\t\t\t\t\t"+e._s(t.name)+"\n\t\t\t\t\t\t")])}))],1),e._v(" "),a("el-col",{staticClass:"content-wrapper",attrs:{span:24}},[a("transition",{attrs:{name:"fade",mode:"out-in"}},[a("router-view")],1)],1)],1)])])],1)},staticRenderFns:[]}},948:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("section",[a("el-col",{staticClass:"toolbar",staticStyle:{"padding-bottom":"0px"},attrs:{span:24}},[a("el-form",{attrs:{inline:!0,model:e.filters}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"项目名称"},model:{value:e.filters.name,callback:function(t){e.$set(e.filters,"name",t)},expression:"filters.name"}})],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary"},on:{click:e.getProjectList}},[e._v("查询")])],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary"},on:{click:e.handleAdd}},[e._v("新增")])],1)],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],staticStyle:{width:"100%",height:"30%"},attrs:{data:e.projectList,"highlight-current-row":""},on:{"selection-change":e.selsChange}},[a("el-table-column",{attrs:{prop:"name",label:"项目名称",width:"250"}}),e._v(" "),a("el-table-column",{attrs:{prop:"round",label:"测试轮次",width:"150",formatter:e.translate,sortable:""}}),e._v(" "),a("el-table-column",{attrs:{prop:"startDate",label:"开始时间",width:"150",formatter:e.dateFormat,sortable:""}}),e._v(" "),a("el-table-column",{attrs:{prop:"finishDate",label:"结束时间",width:"150",formatter:e.dateFormat,sortable:""}}),e._v(" "),a("el-table-column",{attrs:{label:"操作","min-width":"250"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"success",size:"small"},on:{click:function(a){e.goDetails(t.$index,t.row)}}},[e._v("详情")]),e._v(" "),a("el-button",{attrs:{type:"info",size:"small"},on:{click:function(a){e.handleEdit(t.$index,t.row)}}},[e._v("编辑")]),e._v(" "),a("el-button",{attrs:{type:"danger",size:"small"},on:{click:function(a){e.handleDel(t.$index,t.row)}}},[e._v("删除")])]}}])})],1),e._v(" "),a("el-col",{staticClass:"toolbar",attrs:{span:24}},[a("el-pagination",{staticStyle:{float:"right"},attrs:{layout:"prev, pager, next","page-size":20,total:e.total},on:{"current-change":e.handleCurrentChange}})],1),e._v(" "),a("el-dialog",{attrs:{title:"编辑","close-on-click-modal":!1},model:{value:e.editFormVisible,callback:function(t){e.editFormVisible=t},expression:"editFormVisible"}},[a("el-form",{ref:"editForm",attrs:{model:e.editForm,"label-width":"80px",rules:e.editFormRules}},[a("el-form-item",{attrs:{label:"项目名称",prop:"name"}},[a("el-input",{attrs:{"auto-complete":"off"},model:{value:e.editForm.name,callback:function(t){e.$set(e.editForm,"name",t)},expression:"editForm.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"SVN地址",prop:"svnUrl"}},[a("el-input",{attrs:{"auto-complete":"off"},model:{value:e.editForm.svnUrl,callback:function(t){e.$set(e.editForm,"svnUrl",t)},expression:"editForm.svnUrl"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"测试轮次"}},[a("el-select",{attrs:{placeholder:"请选择测试轮次"},model:{value:e.editForm.round,callback:function(t){e.$set(e.editForm,"round",t)},expression:"editForm.round"}},[a("el-option",{attrs:{label:"第一轮",value:"round1"}}),e._v(" "),a("el-option",{attrs:{label:"第二轮",value:"round2"}}),e._v(" "),a("el-option",{attrs:{label:"第三轮",value:"round3"}}),e._v(" "),a("el-option",{attrs:{label:"第四轮",value:"round4"}}),e._v(" "),a("el-option",{attrs:{label:"第五轮",value:"round5"}}),e._v(" "),a("el-option",{attrs:{label:"第六轮",value:"round6"}}),e._v(" "),a("el-option",{attrs:{label:"第七轮",value:"round7"}}),e._v(" "),a("el-option",{attrs:{label:"第八轮",value:"round8"}})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"开始时间"}},[a("el-date-picker",{attrs:{type:"date",placeholder:"选择日期"},model:{value:e.editForm.startDate,callback:function(t){e.$set(e.editForm,"startDate",t)},expression:"editForm.startDate"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"结束时间"}},[a("el-date-picker",{attrs:{type:"date",placeholder:"选择日期"},model:{value:e.editForm.finishDate,callback:function(t){e.$set(e.editForm,"finishDate",t)},expression:"editForm.finishDate"}})],1)],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{nativeOn:{click:function(t){e.editFormVisible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary",loading:e.editLoading},nativeOn:{click:function(t){return e.editSubmit(t)}}},[e._v("提交")])],1)],1),e._v(" "),a("el-dialog",{attrs:{title:"新增项目","close-on-click-modal":!1},model:{value:e.addFormVisible,callback:function(t){e.addFormVisible=t},expression:"addFormVisible"}},[a("el-form",{ref:"addForm",attrs:{model:e.addForm,"label-width":"80px",rules:e.addFormRules}},[a("el-form-item",{attrs:{label:"项目名称",prop:"name"}},[a("el-input",{attrs:{"auto-complete":"off"},model:{value:e.addForm.name,callback:function(t){e.$set(e.addForm,"name",t)},expression:"addForm.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"SVN地址",prop:"svnUrl"}},[a("el-input",{attrs:{"auto-complete":"off"},model:{value:e.addForm.svnUrl,callback:function(t){e.$set(e.addForm,"svnUrl",t)},expression:"addForm.svnUrl"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"测试轮次",prop:"round"}},[a("el-select",{attrs:{placeholder:"请选择测试轮次"},model:{value:e.addForm.round,callback:function(t){e.$set(e.addForm,"round",t)},expression:"addForm.round"}},[a("el-option",{attrs:{label:"第一轮",value:"round1"}}),e._v(" "),a("el-option",{attrs:{label:"第二轮",value:"round2"}}),e._v(" "),a("el-option",{attrs:{label:"第三轮",value:"round3"}}),e._v(" "),a("el-option",{attrs:{label:"第四轮",value:"round4"}}),e._v(" "),a("el-option",{attrs:{label:"第五轮",value:"round5"}}),e._v(" "),a("el-option",{attrs:{label:"第六轮",value:"round6"}}),e._v(" "),a("el-option",{attrs:{label:"第七轮",value:"round7"}}),e._v(" "),a("el-option",{attrs:{label:"第八轮",value:"round8"}})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"开始时间"}},[a("el-date-picker",{attrs:{type:"date",placeholder:"选择日期"},model:{value:e.addForm.startDate,callback:function(t){e.$set(e.addForm,"startDate",t)},expression:"addForm.startDate"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"结束时间"}},[a("el-date-picker",{attrs:{type:"date",placeholder:"选择日期"},model:{value:e.addForm.finishDate,callback:function(t){e.$set(e.addForm,"finishDate",t)},expression:"addForm.finishDate"}})],1)],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{nativeOn:{click:function(t){e.addFormVisible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary",loading:e.addLoading},nativeOn:{click:function(t){return e.addSubmit(t)}}},[e._v("提交")])],1)],1)],1)},staticRenderFns:[]}},949:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement;return(e._self._c||t)("section",[e._v("\n\tmain\n")])},staticRenderFns:[]}},950:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-form",{ref:"ruleForm2",staticClass:"demo-ruleForm login-container",attrs:{model:e.ruleForm2,rules:e.rules2,"label-position":"left","label-width":"0px"}},[a("h3",{staticClass:"title"},[e._v("系统登录")]),e._v(" "),a("el-form-item",{attrs:{prop:"account"}},[a("el-input",{attrs:{type:"text","auto-complete":"off",placeholder:"账号"},model:{value:e.ruleForm2.account,callback:function(t){e.$set(e.ruleForm2,"account",t)},expression:"ruleForm2.account"}})],1),e._v(" "),a("el-form-item",{attrs:{prop:"checkPass"}},[a("el-input",{attrs:{type:"password","auto-complete":"off",placeholder:"密码"},model:{value:e.ruleForm2.checkPass,callback:function(t){e.$set(e.ruleForm2,"checkPass",t)},expression:"ruleForm2.checkPass"}})],1),e._v(" "),a("el-checkbox",{staticClass:"remember",attrs:{checked:""},model:{value:e.checked,callback:function(t){e.checked=t},expression:"checked"}},[e._v("记住密码")]),e._v(" "),a("el-form-item",{staticStyle:{width:"100%"}},[a("el-button",{staticStyle:{width:"100%"},attrs:{type:"primary",loading:e.logining},nativeOn:{click:function(t){return t.preventDefault(),e.handleSubmit2(t)}}},[e._v("登录")])],1)],1)},staticRenderFns:[]}},951:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("section",{staticClass:"chart-container"},[a("el-row",[a("el-col",{attrs:{span:12}},[a("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"chartColumn"}})]),e._v(" "),a("el-col",{attrs:{span:12}},[a("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"chartBar"}})]),e._v(" "),a("el-col",{attrs:{span:12}},[a("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"chartLine"}})]),e._v(" "),a("el-col",{attrs:{span:12}},[a("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"chartPie"}})]),e._v(" "),a("el-col",{attrs:{span:24}},[a("a",{staticStyle:{float:"right"},attrs:{href:"http://echarts.baidu.com/examples.html",target:"_blank"}},[e._v("more>>")])])],1)],1)},staticRenderFns:[]}},952:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{attrs:{id:"app"}},[a("transition",{attrs:{name:"fade",mode:"out-in"}},[a("router-view")],1)],1)},staticRenderFns:[]}}},[420]);
//# sourceMappingURL=app.33f8bfbcee573e721287.js.map