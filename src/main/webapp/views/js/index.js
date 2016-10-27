var southRegion = {
    region: 'south',
    xtype: 'panel',
    split: false,
    height: 30,
    collapsible: true,
    html: 'dddd',
    margins: '0 0 0 0',
 bbar:[
   new Ext.Toolbar.TextItem('工具栏 '),
   {xtype:"tbfill"},
   new Ext.Toolbar.TextItem('XXXX省税务局综合平台'),
   {xtype:'tbseparator'},
   new Ext.Toolbar.TextItem('技术支持 <a href=http://www.163.com>XXXX信息技术有限公司</a>'),
   {pressed:false,text:'与我们联系',iconCls: 'tabs'}
 ]
 
};
function jumpPage(url) {
	$.ajax({
		type : "get",
		url : url,
		data : {
		},
		success : function(data, textStatus) {

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {

		}
	});
}