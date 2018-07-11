<template>
	<el-form ref="form" :model="form" label-width="80px" @submit.prevent="onSubmit" style="margin:20px;width:60%;min-width:600px;">
		<el-form-item label="用例集合">
			<el-select v-model="form.region" placeholder="请选择用例集合">
				<el-option label="区域一" value="shanghai"></el-option>
				<el-option label="区域二" value="beijing"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="用例分页">
			<el-select v-model="form.region" placeholder="请选择用例分页">
				<el-option label="区域一" value="shanghai"></el-option>
				<el-option label="区域二" value="beijing"></el-option>
			</el-select>
		</el-form-item>
		<el-form-item label="执行人">
			<el-input v-model="form.name" ></el-input>
		</el-form-item>
		<el-form-item label="用例等级">
			<el-checkbox-group v-model="form.type">
				<el-checkbox label="L1" name="type"></el-checkbox>
				<el-checkbox label="L2" name="type"></el-checkbox>
				<el-checkbox label="L3" name="type"></el-checkbox>
				<el-checkbox label="L4" name="type"></el-checkbox>
			</el-checkbox-group>
		</el-form-item>
		<el-form-item>
			<el-button type="primary">立即创建</el-button>
			<el-button @click.native.prevent>取消</el-button>
		</el-form-item>
	</el-form>
</template>

<script>
	export default {
		data() {
			return {
				form: {
					name: '',
					region: '',
					date1: '',
					date2: '',
					delivery: false,
					type: [],
					resource: '',
					desc: ''
				}
			}
		},
		methods: {
			onSubmit() {
				console.log('submit!');
			}
		}
	}

</script>