
<style type="text/css">
	#keyword {
		display: inline;
		width: 250px;
		margin-left: 0px;
		margin-right: 15px;
		float: left;
	}
	
	.select {
		background: #36C;
		color: #FFF;
	}
	
	.select span {
		color: #FFF;
	}
</style>
<script type="text/javascript">
<!--
	    var j=-1;
		var temp_str;
		
		var $ById=function(node){
			return document.getElementById(node);
		}
		
		var $$=function(node){
			return document.getElementsByTagName(node);
		}
		
		function ajaxSuggest(){
			if($ById("q").value==""){
				return ;
			}
			
			var xmlhttp;
			try{
				xmlhttp=new XMLHttpRequest();
				}
			catch(e){
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
				}
			xmlhttp.onreadystatechange=function(){
			if (xmlhttp.readyState==4){
				if (xmlhttp.status==200){
					var data=xmlhttp.responseText;
					$ById("suggest").innerHTML=data;
					j=-1;
					}
				}
			}
			var q = $ById("q").value;
			var currentSolrName  = $ById("currentSolrName").value; 
			xmlhttp.open("post", "/Solr/"+currentSolrName+"/admin/TestSuggestServlet.so", true);
			xmlhttp.setRequestHeader('Content-type','application/x-www-form-urlencoded');
			xmlhttp.send("q="+q);
		}
		function keyupDeal(e){
			var keyc;
			if(window.event){
				keyc=e.keyCode;
				}
			else if(e.which){
				keyc=e.which;
				}
			if(keyc!=40 && keyc!=38){
				ajaxSuggest();
				temp_str=$ById("q").value;
			}
	   }

		function setStyle(num){
			
			for(var i=0;i<$$("li").length;i++){
				var li_node=$$("li")[i];
				li_node.className="";
			}
			if(j>=0 && j<$$("li").length){
				var i_node=$$("li")[j];
				$$("li")[j].className="select";
			}
				
	    }
			
		function mover(nodevalue){
			j=nodevalue;
			setStyle(j);
		
		}
		
		function hide_suggest(){
			var nodes=document.body.childNodes
			for(var i=0;i<nodes.length;i++){
				if(nodes[i]!=$ById("q")){
					$ById("suggest").innerHTML="";
					}
				}
		}
			
	    function keydownDeal(e){
	    alert("SSSSSSSSSSSSS");
			var keyc;
			if(window.event){
				keyc=e.keyCode;
				}
			else if(e.which){
				keyc=e.which;
				}
			if(keyc==40 || keyc==38){
				if(keyc==40){
					if(j<$$("li").length){
						j++;
						if(j>=$$("li").length){
							j=-1;
						}
					}
					if(j>=$$("li").length){
							j=-1;
						}
				}
				if(keyc==38){
					if(j>=0){
						j--;
						if(j<=-1){
							j=$$("li").length;
						}
					}
					else{
						j=$$("li").length-1;
					}
				}
				setStyle(j);
				if(j>=0 && j<$$("li").length){
					$ById("q").value=$$("li")[j].childNodes[0].nodeValue;
					}
				else{
					$ById("q").value=temp_str;
					}
			}
			
			if(keyc==13){
				set(j);
			}
		} 
		
		
		function set(i){
			var obj=document.getElementById("li_"+i);
			
			$ById("q").value=obj.attributes["context"].value;
			
			hide_suggest();
			selectObj.focus();
			
		}
		 
	// -->
</script>



<input type="text" name="q" id="q" onkeyup="keyupDeal(event);" onkeydown="keydowndeal(event);" onclick="javaScript:keyupDeal(event);" autocomplete="off" size="34"/>
<div id="suggest" style="position: absolute; margin-top: 6px; background: #CCE8CF;"></div>

