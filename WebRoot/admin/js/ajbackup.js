  ///////////////////////////////////////////显示备份////////////////////////////////////////////
 	function loaddata(){
 		emptyElement("list");
 		var url="AjaxLoadBackup.so?"+new Date();
 		 $.getJSON(url,function(data){
 		 var str="<td>还原点</td><td>";
 			for(var i=0;i<data.length;i++){
 		 		str=str+"<input type='radio' name='backupname' value='"+data[i]+"'>"+data[i]+"<br>";
 		 		}
 		 		str+="</td>";
				 $("#list").append(str);
            });
 	}
 	function emptyElement(id){
 		$("#"+id+"").empty();
 	}
//////////////////////////////////////////显示任务单/////////////////////////////////////////      
 function loadjobs(){
   var url="ShowCurrentJobsServlet?"+new Date();
   var table2=$("#joblist");
 		$.getJSON(url,function(data){
					  if(data.length>0){
					  		 table2.append("<tr><td>任务名称</td><td>开始时间</td><td>结束时间</td><td>状态</td><td>操作</td></tr>");
					  		 for(var i=0;i<data.length;i++){
					  		 		var name="'"+data[i].name+"'";
					  		 		var h;
											if("wait"==data[i].state||"running"==data[i].state){
												h='<a href="javascript:jobOp('+name+')";>停用</a>';
											}
											if("stop"==data[i].state){
												h='<a href="javascript:deljob('+name+')";>删除</a>';
											}
					  		 	 table2.append("<tr id='"+data[i].name+"'><td>"+data[i].name+"</td><td>"+data[i].start+"</td><td>"+data[i].end+"</td><td>"+data[i].state+"</td><td>"+h+"</td></tr>");
					  		 }
					  }
            });
 }
  ///////////////////////////////////////////////停止任务////////////////////////////////////////////////       
  function jobOp(jobname){
  		var url="StopCurrentJob?jobname="+jobname+"&"+new Date();
	 		$.get(url,function(data){
						alert(data);
	            });
 	}
 ////////////////////////////////////////////////////删除任务//////////////////////////////////////    
   function deljob(jobname){
  		var url="DelJobServlet?jobname="+jobname+"&"+new Date();
	 		$.get(url,function(data){
						$("#"+jobname+"").hide();
	            });
 			}