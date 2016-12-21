  function selAll(obj){
 	 
 	var el = document.getElementsByName('checkbox');
 	var len = el.length; 
 	 
	if(obj.checked){
		for(var i=0;i<len;i++){
			el[i].checked = true;
		}
	}else{
		for(var i=0;i<len;i++){
			el[i].checked = false;
		}
	}
 }
  
   var xmlHttp;
  
  function createHttp(){
       if(window.ActiveXObject){
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
       }
      if(window.XMLHttpRequest){
            xmlHttp=new XMLHttpRequest();
      }
  }
 
  function delsel(dela){
 	  if(!confirm("是否删除选中索引？")){
 	  	return ;
 	  }
 	  var ids="";
	  var el = document.getElementsByName('checkbox');
	  var len = el.length; 
	  for(var i=0;i<len;i++){
	 	if(el[i].checked){
	 	 
	 		ids=ids+el[i].id+",";
	 	}
	  }
      createHttp();
      var url="BatchDeleteIndex.so?ids="+ ids+"&data="+new Date();
      xmlHttp.open("GET",url,true);
      xmlHttp.onreadystatechange=callback;
      xmlHttp.send(null);
  }
  
  function callback(){
      if(xmlHttp.readyState==4){
          if(xmlHttp.status==200){
            	var backText=xmlHttp.responseText;
             	if(backText!=""){
             		var ids=backText.split(",");
             	 	var len=ids.length;
             		for(var i=0;i<len-1;i++){
             			$("#"+ids[i]+"_id").hide();
             		}
             		 
              	}
              }
          }
      }
