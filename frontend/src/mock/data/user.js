import Mock from 'mockjs';
const LoginUsers = [
  {
    id: 1,
    username: 'admin',
    password: '123456',
    avatar: 'https://raw.githubusercontent.com/taylorchen709/markdown-images/master/vueadmin/user.png',
    name: '张某某'
  }
];

const Projects = [];
const RoundArray = ['第一轮','第二轮'];
// for (let i = 0; i < 6; i++) {
//   Projects.push(Mock.mock({
//     id: Mock.Random.guid(),
//     name: '数字学习中心1.0',
//     'round|1': RoundArray,
//     beginDate: Mock.Random.date('yyyy-MM-dd'),
//     endDate: Mock.Random.date('yyyy-MM-dd')
//   }));
// }

Projects.push(Mock.mock({
  id: Mock.Random.guid(),
  name: '数字学习中心1.0',
  'round': '第一轮',
  beginDate: '2018-06-01',
  endDate: '2018-06-30'
}));
Projects.push(Mock.mock({
  id: Mock.Random.guid(),
  name: '数字学习中心1.0',
  'round': '第二轮',
  beginDate: '2018-07-01',
  endDate: '2018-07-30'
}));


//optionsForPackage: [{value:'1',label:'1'},{value:'2',label:'2'},{value:'3',label:'3'}],
//optionsForModule: [{value:'1',label:'1'},{value:'2',label:'2'},{value:'3',label:'3'}],
//optionsForPriority: ['L0','L1'],

//{"packages":[{"id":1,"value":"测试集合1"},{"id":2,"value":"测试集合2"}]，
//"1":["模块1","模块2"],
//"2":["模块11","模块22"]
//}
const optionsForPackage = ['package1','package2','package3','package4','package5','package6','package7','package8','package9','package10','package11'];
const optionsForModule = ['module1','module2','module3','module4','module5','module6','module7','module8'];
const optionsForPriority = ['L0','L1','L2','L3','L4'];
const Options = {"packages":[{"id":'p1',"value":"测试集合1"},{"id":'p2',"value":"测试集合2"}],
"p1":[{value:"模块1",id:'m1'},{value:"模块2",id:'m2'}],
"p2":[{value:"模块21",id:'m1'},{value:"模块22",id:'m2'}],
"p1m1":['L0','L1'],
"p1m2":['L0','L1','L2'],
"p2m1":['L0'],
"p2m2":['L0','L1','L2']
}

// Options.optionsForPackage = optionsForPackage;
// Options.optionsForModule = optionsForModule;
// Options.optionsForPriority = optionsForPriority;


export { LoginUsers, Projects, Options };
