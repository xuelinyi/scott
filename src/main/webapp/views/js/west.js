function testFunction(){
 alert('This function need to be modified.');
}
/** 管理单元面板 */
var manageTree = new Ext.tree.TreePanel( {
    autoScroll: true,
    animate: false,
    enableDD: true,
    containerScroll: true,
    rootVisible: false,
    height:350
});
/** 管理单元树根 */
var manageRoot = new Ext.tree.TreeNode({
 id: '0',
 text: '',
 leaf: false
});
/** 收购品种管理子树根 */
var sgpzRoot = new Ext.tree.TreeNode({
 id: '01',
 text: '管理收购品种',
 leaf: false,
 cls: 'folder'
});
sgpzRoot.appendChild(new Ext.tree.TreeNode({
 id: '001',
 text: '品种列表',
 leaf: true,
 href: "javascript: testFunction();"
}));
sgpzRoot.appendChild(new Ext.tree.TreeNode({
 id: '002',
 text: '添加品种',
 leaf: true,
 href: 'javascript: testFunction();'
}));
manageRoot.appendChild(sgpzRoot);
/**<预留的备用代码> */
manageRoot.appendChild(new Ext.tree.TreeNode({
 id: '11',
 text: '上传xml',
 leaf: true,
 href: 'javascript: testFunction();' 
}));
manageRoot.appendChild(new Ext.tree.TreeNode({
 id: '21',
 text: '收购信息',
 leaf: true,
 href: 'javascript: testFunction();' 
}));

/** 公司信息管理树根 */
var manageCompanyRoot = new Ext.tree.TreeNode({
 id: '03',
 text: '管理公司信息',
 leaf: false,
 cls: 'folder'
});
manageCompanyRoot.appendChild(new Ext.tree.TreeNode({
 id: '031',
 text: '查询公司信息',
 leaf: true,
 href: 'javascript: testFunction();'
}));
manageCompanyRoot.appendChild(new Ext.tree.TreeNode({
 id: '032',
 text: '添加公司',
 leaf: true,
 href: 'javascript: testFunction();'
}));
manageRoot.appendChild(manageCompanyRoot);
manageTree.setRootNode(manageRoot);

var msgTree = new Ext.tree.TreePanel( {
   title: '未阅信息',
   iconCls: 'tabs',
    autoScroll: true,
    animate: false,
    enableDD: true,
    containerScroll: true,
    rootVisible: false,
    height:300
});
var msgRoot = new Ext.tree.TreeNode({
 id: '0',
 text: '',
 leaf: false
});
msgRoot.appendChild(new Ext.tree.TreeNode({
 id: '11',
 text: '代办流程',
 leaf: true,
 href: 'javascript: gotoUploadXML();' 
}));
msgRoot.appendChild(new Ext.tree.TreeNode({
 id: '11',
 text: '未读消息',
 leaf: true,
 href: 'javascript: gotoUploadXML();' 
}));
msgRoot.appendChild(new Ext.tree.TreeNode({
 id: '11',
 text: '今日日程',
 leaf: true,
 href: 'javascript: gotoUploadXML();' 
}));
msgTree.setRootNode(msgRoot);
//定义并构造西部面板
var westRegion = {
    region:'west',
    id:'west-panel',
    iconCls: 'tabs',
    title:'客户管理',
    split:true,
    width: 200,
    minSize: 175,
    maxSize: 400,
    collapsible: true,
    margins:'0 0 0 5',
    layout:'column',
    layoutConfig:{
        animate:true
    },
    items: [
        {
         
         items:manageTree
        },
        {
         items:msgTree
        }
        
        //manageTree
    ]
};