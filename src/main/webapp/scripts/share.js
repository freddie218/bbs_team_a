    function shareClick(postTitle,postContent) {
        var rrShareParam = {
            resourceUrl : '',	//分享的资源Url
            srcUrl : '',	//分享的资源来源Url,默认为header中的Referer
            pic : '',		//分享的主题图片Url
            title : postTitle,		//分享的标题
            description : postContent	//分享的详细描述
        };
        rrShareOnclick(rrShareParam);
    }

    function shareToDouban(){
        var d=document,
            e=encodeURIComponent,
            s1=window.getSelection,
            s2=d.getSelection,
            s3=d.selection,
            s=s1?s1():s2?s2():s3?s3.createRange().text:'',
            r='http://www.douban.com/recommend/?url='+e(d.location.href)+'&title='+e(d.title)+'&sel='+e(s)+'&v=1',
            w=450,h=330,
            x=function()
            {
                if(!window.open(r,'douban','toolbar=0,resizable=1,scrollbars=yes,status=1,width='+w+',height='+h+',left='+(screen.width-w)/2+',top='+(screen.height-h)/2))
                location.href=r+'&r=1'
            };
            if(/Firefox/.test(navigator.userAgent))
            {setTimeout(x,0)}
            else{x()}
    }

