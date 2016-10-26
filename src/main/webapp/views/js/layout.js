Ext.onReady(function(){
    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
    var button = Ext.get('show-btn');
    button.on('click', function(){
        // tabs for the center
        var tabs = new Ext.TabPanel({
           //如果你用border布局, 则子元素必须设置region来确定他们各自的位置
            region    : 'center',
            margins   : '3 3 3 0', 
            activeTab : 0,
            defaults  : {
        autoScroll : true
      },
            items     : [{
                title    : 'Bogus Tab',
                html     : Ext.example.bogusMarkup
             },{
                title    : 'Another Tab',
                html     : Ext.example.bogusMarkup
             },{ 
                title    : 'Closable Tab',
                html     : Ext.example.bogusMarkup,
                closable : true
            }]
        });
        // Panel for the west
        var nav = new Ext.Panel({
            title       : 'Navigation',
            //如果你用border布局, 则子元素必须设置region来确定他们各自的位置
            region      : 'north',
            split       : true,
            height      : 200,
            collapsible : true,
            margins     : '3 0 3 3',
            cmargins    : '3 3 3 3'
        });
        var win = new Ext.Window({
            title    : 'Layout Window',
            closable : true,
            width    : 600,
            height   : 650,
            plain    : true,
            //设置border布局
            layout   : 'border',
            items    : [nav, tabs]
        });
        win.show(button);
    });
    //Ext.state.Manager.set('mykey1', 'this is my key');
    //alert(Ext.state.Manager.get('mykey1', 'norecord'));
});