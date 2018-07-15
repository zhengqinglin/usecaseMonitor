<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.name" placeholder="用例名称"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" v-on:click="getTestReport">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<template>
			<el-table :data="reports" highlight-current-row v-loading="loading" fit style="width: 100%;min-height:500px;">
				<el-table-column type="index" width="60">
				</el-table-column>
				<el-table-column prop="usecasePackage" label="用例包名称" width="140" sortable>
				</el-table-column>
				<el-table-column prop="module" label="功能模块" width="120">
				</el-table-column>
				<el-table-column prop="executor" label="执行人" width="80">
				</el-table-column>
				<el-table-column prop="total" label="用例总数" width="100">
				</el-table-column>
				<el-table-column prop="priorities" label="用例等级" width="100">
				</el-table-column>
				<el-table-column prop="pass" label="PASS数" width="100">
				</el-table-column>
				<el-table-column prop="fail" label="FAIL数" width="100">
				</el-table-column>
				<el-table-column prop="finishRate" label="完成进度(%)" min-width="150">
					<template scope="scope">
						<el-progress :percentage="scope.row.finishRate" :show-text='true'></el-progress>
					</template>
				</el-table-column>
				<el-table-column label="操作" min-width="150">
				<template scope="scope">
					<el-button type="primary" size="small" @click="refresh(scope.$index, scope.row)">刷新</el-button>
					<!-- <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button> -->
					<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
			</el-table>
		</template>
		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<!-- <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button> -->
			<el-pagination layout="total,prev, pager, next" @current-change="handleCurrentChange" :page-size="10" :total="total" style="float:right;">
			</el-pagination>
		</el-col>
		<el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
			<el-form :model="addForm" ref="addForm" label-width="80px" @submit.prevent="onSubmit" style="margin:20px;width:60%;min-width:600px;">
					<el-form-item label="用例包">
						<el-select v-model="addForm.usecasePackage" placeholder="请选择用例包" :loading="loaded" loading-text="数据加载中" @change="selectPackage">
							<el-option
								v-for="item in optionsForPackage"
								:key="item.id"
								:label="item.value"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="功能模块">
						<el-select v-model="addForm.module" placeholder="请选择功能模块">
							<el-option
								v-for="module in optionsForModule"
								:key="module.id"
								:label="module.value"
								:value="module.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="执行人">
						<el-col :span='10'>
							<el-input v-model="addForm.executor" width="10px" ></el-input>
						</el-col>
					</el-form-item>
					<el-form-item label="用例等级">
						<el-checkbox-group v-model="addForm.priorities">
							<el-checkbox v-for="level in optionsForPriority" :label="level" :key="level">{{level}}</el-checkbox>
						</el-checkbox-group>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click.native="addSubmit" :loading="addLoading">立即创建</el-button>
						<el-button @click="addFormVisible = false">取消</el-button>
					</el-form-item>
			</el-form>
		</el-dialog>
	</section>
</template>
<script>
	import { getTestReportList, getOption, addTestReport, removeTestReport, getTestReport } from '../../api/api';
	//import NProgress from 'nprogress'
	export default {
		data() {
			return {
				filters: {
					name: ''
				},
				total:0,
				page: 1,
				options:{},
				optionsForPackage: [],
				optionsForModule: '',
				optionsForPriority: ['0','1','2','3'],
				loading: false,
				reports: [
				],
				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				//新增界面数据
				addForm: {
					usecasePackage: '',
					module: '',
					executor: '',
					priorities: []
				},
				//加载数据标志位
				loaded:false
			}
		},
		methods: {
			//选择用例包
			selectPackage: function (packageValue) {
				console.log(packageValue)
				console.log(this.options)
				this.optionsForModule = this.options[packageValue];
				console.log(this.optionsForModule)
			},
			//显示新增界面
			handleAdd: function () {
				this.addFormVisible = true;
				//后台请求
				this.getOptions();
				this.addForm = {
					usecasePackage: '',
					module: '',
					executor: '',
					priorities: [],
					projectId:this.$route.query.id
				};
				
			},
			addSubmit: function () {
				this.$refs.addForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.addLoading = true;
							//NProgress.start();
							let para = Object.assign({}, this.addForm);
							console.log(para)
							para.priorities=this.addForm.priorities.join(",");
							console.log(para) 
							addTestReport(para).then((res) => {
								this.addLoading = false;
								//NProgress.done();
								this.$message({
									message: '提交成功',
									type: 'success'
								});
								this.$refs['addForm'].resetFields();
								this.addFormVisible = false;
								this.getTestReport();
							});
						});
					}
				});
			},
			//刷新
			refresh: function (index, row) {
					this.listLoading = true;
					//NProgress.start();
					let para = row.id;
					getTestReport(para).then((res) => {
						this.listLoading = false;
						//NProgress.done();
						this.$message({
							message: '刷新成功',
							type: 'success'
						});
						this.getTestReport();
					});
			},
			
			//删除
			handleDel: function (index, row) {
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					//NProgress.start();
					let para = row.id;
					removeTestReport(para).then((res) => {
						this.listLoading = false;
						//NProgress.done();
						this.$message({
							message: '删除成功',
							type: 'success'
						});
						this.getTestReport();
					});
				}).catch(() => {

				});
			},
			//获取可选的值
			getOptions: function () {
				let para = this.$route.query.id;
				this.loaded = true;
				getOption(para).then((res) => {
					console.log(res)
					this.loaded = false;
					this.options = res.data.options;
					this.optionsForPackage = this.options.packages;
				})
			},
			//获取测试报告列表，根据前一个页面的项目，找到对应的测试报告列表
			getTestReport: function () {
				// this.reports = [{index:0,name:"cmr",page:"随堂测试",person:"高燕丽",total:100,finish:50,pass:49,fail:1,progress:"50%"}]

				//如何获取父页面传递过来的值
				console.log(this.$route.query.id)
				// let para = this.$route.query.id;
				let para = {
					id: this.$route.query.id,
					pageNum: this.page,
					name: this.filters.name
				};
				console.log(para);
				
				this.loading = true;
				//NProgress.start();
				getTestReportList(para).then((res) => {
					console.log(res)
					this.total = res.data.total;
					this.reports = res.data.list;
					this.loading = false;
					//NProgress.done();
				});
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getTestReport();
			}
		},
		mounted() {
			this.getTestReport()
		}
	};

</script>

<style scoped>

</style>