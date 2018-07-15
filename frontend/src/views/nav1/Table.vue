<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.name" placeholder="项目名称"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" v-on:click="getProjectList">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="projectList" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;height:30%">
			<!-- <el-table-column type="selection" width="55">
			</el-table-column> -->
			<!-- <el-table-column type="id" width="60">
			</el-table-column> -->
			<el-table-column prop="name" label="项目名称" width="250" >
			</el-table-column>
			<el-table-column prop="round" label="测试轮次" width="150" :formatter="translate" sortable>
			</el-table-column>
			<el-table-column prop="startDate" label="开始时间" width="150" :formatter="dateFormat" sortable>
			</el-table-column>
			<el-table-column prop="finishDate" label="结束时间" width="150" :formatter="dateFormat" sortable>
			</el-table-column>
			<el-table-column label="操作" min-width="250">
				<template scope="scope">
					<el-button type="success" size="small" @click="goDetails(scope.$index, scope.row)">详情</el-button>
					<el-button type="info" size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<!-- <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button> -->
			<el-pagination layout="total,prev, pager, next" @current-change="handleCurrentChange" :page-size="10" :total="total" style="float:right;">
			</el-pagination>
		</el-col>

		<!--编辑界面-->
		<el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
				<el-form-item label="项目名称" prop="name">
					<el-input v-model="editForm.name" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="SVN地址" prop="svnUrl">
					<el-input v-model="editForm.svnUrl" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="测试轮次">
					<el-select v-model="editForm.round" placeholder="请选择测试轮次">
						<el-option label="第一轮" value="round1"></el-option>
						<el-option label="第二轮" value="round2"></el-option>
						<el-option label="第三轮" value="round3"></el-option>
						<el-option label="第四轮" value="round4"></el-option>
						<el-option label="第五轮" value="round5"></el-option>
						<el-option label="第六轮" value="round6"></el-option>
						<el-option label="第七轮" value="round7"></el-option>
						<el-option label="第八轮" value="round8"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="开始时间">
					<el-date-picker type="date" placeholder="选择日期" v-model="editForm.startDate"></el-date-picker>
				</el-form-item>
				<el-form-item label="结束时间">
					<el-date-picker type="date" placeholder="选择日期" v-model="editForm.finishDate"></el-date-picker>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>

		<!--新增界面-->
		<el-dialog title="新增项目" v-model="addFormVisible" :close-on-click-modal="false">
			<el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
				<el-form-item label="项目名称" prop="name">
					<el-input v-model="addForm.name" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="SVN地址" prop="svnUrl">
					<el-input v-model="addForm.svnUrl" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="测试轮次" prop="round">
					<el-select v-model="addForm.round" placeholder="请选择测试轮次">
						<el-option label="第一轮" value="round1"></el-option>
						<el-option label="第二轮" value="round2"></el-option>
						<el-option label="第三轮" value="round3"></el-option>
						<el-option label="第四轮" value="round4"></el-option>
						<el-option label="第五轮" value="round5"></el-option>
						<el-option label="第六轮" value="round6"></el-option>
						<el-option label="第七轮" value="round7"></el-option>
						<el-option label="第八轮" value="round8"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="开始时间">
					<el-date-picker type="date" placeholder="选择日期" v-model="addForm.startDate"></el-date-picker>
				</el-form-item>
				<el-form-item label="结束时间">
					<el-date-picker type="date" placeholder="选择日期" v-model="addForm.finishDate"></el-date-picker>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="addFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
	import util from '../../common/js/util'
	import axios from 'axios';
	//import NProgress from 'nprogress'
	import { getProjectList, removeProject, addProject, editProject } from '../../api/api';

	export default {
		data() {
			return {
				filters: {
					name: ''
				},
				projectList: [],
				total: 0,
				page: 1,
				listLoading: false,
				sels: [],//列表选中列

				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					name: [
						{ required: true, message: '请输入项目名称', trigger: 'blur' }
					],
					svnUrl: [
						{ required: true, message: '请输入SVN地址', trigger: 'blur' }
					],
					round: [
						{ required: true, message: '请选择测试轮次', trigger: 'blur' }
					]
					
				},
				//编辑界面数据
				editForm: {
					id: 0,
					name: '',
					round: '',
					startDate: '',
					finishDate: ''
				},

				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
					name: [
						{ required: true, message: '请输入项目名称', trigger: 'blur' }
					],
					svnUrl: [
						{ required: true, message: '请输入SVN地址', trigger: 'blur' }
					],
					round: [
						{ required: true, message: '请选择测试轮次', trigger: 'blur' }
					]
				},
				//新增界面数据
				addForm: {
					name: '',
					round: '',
					startDate: '',
					finishDate: ''
				}

			}
		},
		methods: {
			translate:function(row, column) {
				var round = row[column.property];
				switch(round){
					case 'round1':
						return "第一轮";
					case 'round2':
						return "第二轮";
					case 'round3':
						return "第三轮";
					case 'round4':
						return "第四轮";
					case 'round5':
						return "第五轮";
					case 'round6':
						return "第六轮";
					case 'round7':
						return "第七轮";
					case 'round8':
						return "第八轮";
				}
			},
			dateFormat:function(row, column) {
				var date = row[column.property];
				if (date == undefined) {
					return "";
				}
				return util.formatDate.format(new Date(date), 'yyyy-MM-dd');
            },
			handleCurrentChange(val) {
				this.page = val;
				this.getProjectList();
			},
			//获取项目列表
			getProjectList() {
				let para = {
					pageNum: this.page,
					name: this.filters.name
				};
				this.listLoading = true;
				//NProgress.start();
				// axios.get('http://127.0.0.1:8081/project', { params: para }).then((res)=>{
				// 	console.log(res)
				// 	this.total = res.data.total;
				// 	this.projectList = res.data.projectList;
				// 	this.listLoading = false;
				// }).catch(function (response){
				// 	console.log(response)
				// });
				getProjectList(para).then((res) => {
					console.log(res)
					
					this.total = res.data.total;
					this.projectList = res.data.list;
					this.listLoading = false;
					//NProgress.done();
				});
			},
			//跳转到详情页面
			goDetails: function (index, row) {
				// alert(row.id)
				this.$router.push({path:'/user', query:{id:row.id}})
			},
			//删除
			handleDel: function (index, row) {
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					//NProgress.start();
					let para = row.id;
					removeProject(para).then((res) => {
						this.listLoading = false;
						//NProgress.done();
						this.$message({
							message: '删除成功',
							type: 'success'
						});
						this.getProjectList();
					});
				}).catch(() => {

				});
			},
			//显示编辑界面
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//显示新增界面
			handleAdd: function () {
				this.addFormVisible = true;
				this.addForm = {
					name: '',
					svnUrl:'',
					round: '',
					startDate: '',
					finishDate: ''
				};
			},
			//编辑
			editSubmit: function () {
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.editLoading = true;
							//NProgress.start();
							let para = Object.assign({}, this.editForm);
							para.startDate = (!para.startDate || para.startDate == '') ? '' : util.formatDate.format(new Date(para.startDate), 'yyyy-MM-dd');
							para.finishDate = (!para.finishDate || para.finishDate == '') ? '' : util.formatDate.format(new Date(para.finishDate), 'yyyy-MM-dd');
							//finishDate要保证大于startDate 
							editProject(para).then((res) => {
								this.editLoading = false;
								//NProgress.done();
								this.$message({
									message: '提交成功',
									type: 'success'
								});
								this.$refs['editForm'].resetFields();
								this.editFormVisible = false;
								this.getProjectList();
							});
						});
					}
				});
			},
			//新增
			addSubmit: function () {
				this.$refs.addForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.addLoading = true;
							//NProgress.start();
							let para = Object.assign({}, this.addForm);
							para.startDate = (!para.startDate || para.startDate == '') ? '' : util.formatDate.format(new Date(para.startDate), 'yyyy-MM-dd');
							para.finishDate = (!para.finishDate || para.finishDate == '') ? '' : util.formatDate.format(new Date(para.finishDate), 'yyyy-MM-dd');
							//finishDate要保证大于startDate 
							addProject(para).then((res) => {
								console.log(res)
								this.addLoading = false;
								//NProgress.done();
								if (res.status && res.status == 200 && res.data.success) {
									this.$message({
									message: '提交成功',
									type: 'success'
									});
									this.$refs['addForm'].resetFields();
									this.addFormVisible = false;
									this.getProjectList();
								} else {
									this.$alert(res.data.message, {});
								}
						
								
							});
						});
					}
				});
			},
			selsChange: function (sels) {
				this.sels = sels;
			},
			//批量删除
			batchRemove: function () {
				var ids = this.sels.map(item => item.id).toString();
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					//NProgress.start();
					let para = { ids: ids };
					batchRemoveUser(para).then((res) => {
						this.listLoading = false;
						//NProgress.done();
						this.$message({
							message: '删除成功',
							type: 'success'
						});
						this.getProjectList();
					});
				}).catch(() => {

				});
			}
		},
		mounted() {
			this.getProjectList();
		}
	}

</script>

<style scoped>

</style>