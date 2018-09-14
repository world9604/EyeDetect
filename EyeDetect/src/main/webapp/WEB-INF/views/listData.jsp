<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<%@ include file="/WEB-INF/include/include-header.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<h1>Hello world!</h1>

<!-- <a href="#this" class="btn" id="downloadAllImage">이미지 전체 다운로드</a>
<a href="#this" class="btn" id="downloadAllSensor">센서값 전체 다운로드</a>
<a href="#this" class="btn" id="deleteAll">데이터 전체 삭제</a> -->

<div style="float:right;">
   	<input type="text" id="dataTypeInput" placeholder="dataType">
	<input type="text" id="labelInput" placeholder="label">
	<a href="#this" class="btn" id="search">search</a>
	<a href="#this" class="btn" id="explain">explain</a>
</div>

<br/>
<br/>
<br/>

<div class="pagination" id="paging"></div>

<br/>
<br/>
<br/>

<table class="board_list" style="border:1px solid #ccc">
    <colgroup>
    	<col width="10%"/>
        <col width="10%"/>
        <col width="10%"/>
        <col width="10%"/>
        <col width="10%"/>
        <col width="10%"/>
        <col width="10%"/>
        <col width="10%"/>
        <col width="10%"/>
    </colgroup>
    <thead>
        <tr>	
        	<th scope="col">id</th>
            <th scope="col">image</th>
            <th scope="col">type</th>
            <th scope="col">label</th>
            <th scope="col">extraData</th>
            <th scope="col">register date</th>
            <th scope="col">btn</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${fn:length(sensors) > 0}">
                <c:forEach items="${sensors}" var="sensor">
                    <tr data="sensor">
                    	<td data="sensorId">${sensor.idx}</td>
                     	<td data="img">
                     		<img width="200" data="${sensor.idx}" src='data:image/png;base64,${sensor.encodedImage}' alt="image" />
                   		</td>
                   		<td data="dataType">${sensor.dataType}</td>
                        <td data="label">${sensor.label}</td>
                        <td data="label">${sensor.extraData}</td>
                        <td data="registerDate">${sensor.registerDate}</td>
						<td>
							<input type="hidden" data="id" value="${sensor.idx}">
							<a href="#this" class="btn" data="downloadImage">이미지 저장</a>
							<br/>
							<a href="#this" class="btn" data="downloadSensor">센서값 저장</a>
							<br/>
							<a href="#this" class="btn" data="delete">삭제</a>
						</td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="6">조회된 결과가 없습니다.</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>
     
    <%@ include file="/WEB-INF/include/include-body.jsp" %>
    <script type="text/javascript">
    
        function paging(totalData, dataPerPage, pageCount, currentPage){
             
             console.log("currentPage : " + currentPage);
             
             var totalPage = Math.ceil(totalData/dataPerPage);    // 총 페이지 수
             var pageGroup = Math.ceil(currentPage/pageCount);    // 페이지 그룹
             var last = pageGroup * pageCount;    // 화면에 보여질 마지막 페이지 번호
             
             if(last > totalPage) last = totalPage;
             
             var first = last - (pageCount - 1);    // 화면에 보여질 첫번째 페이지 번호
            
             if(first < 1) first = 1;
             
             var next = last + 1;
             var prev = first - 1;
             
             console.log("last : " + last);
             console.log("first : " + first);
             console.log("next : " + next);
             console.log("prev : " + prev);
             
             var $pingingView = $("#paging");
             var html = "";
             
             if(prev > 0)
                 html += "<a href=# id='prev'><</a> ";
             
             for(var i=first; i <= last; i++){
                 html += "<a href='#' id=" + i + ">" + i + "</a> ";
             }
             
             if(last < totalPage)
                 html += "<a href=# id='next'>></a>";
             
             $("#paging").html(html);    // 페이지 목록 생성
             
             $("#paging a#" + currentPage).addClass("active");;    // 현재 페이지 표시
                                                
             $("#paging a").click(function(){
                 
                 var $item = $(this);
                 var $id = $item.attr("id");
                 var selectedPage = $item.text();
                 
                 if($id == "next") selectedPage = next;
                 if($id == "prev") selectedPage = prev;
                 
                 var startData = (selectedPage - 1) * dataPerPage;
                 
                 console.log("selectedPage : " + selectedPage);
                 console.log("dataPerPage : " + dataPerPage);
                 console.log("startData : " + startData);
                 
                 fn_paging(selectedPage, dataPerPage, startData);
             });
                                                
         }
         
         function fn_paging(selectedPage, dataPerPage, startData){
           	   var comSubmit = new ComSubmit();
               comSubmit.setUrl("<c:url value='/showData.do' />");
               comSubmit.addParam("selectedPage", selectedPage);
               comSubmit.addParam("dataPerPage", dataPerPage);
               comSubmit.addParam("startData", startData);
               comSubmit.submit();
          }
         
         function fn_goToInform(dataType, label){
         	 var comSubmit = new ComSubmit();
             comSubmit.setUrl("<c:url value='/goToInform.do' />");
             comSubmit.submit();
          }
         
         function fn_search(dataType, label){
        	var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/searchData.do' />");
            comSubmit.addParam("dataType", dataType);
            comSubmit.addParam("label", label);
            comSubmit.submit();
         }
         
         function fn_downloadJsonTxt(obj){
	   		$("<a />", {
	   		  "download": "data.json",
	   		  "href" : "data:application/json," + encodeURIComponent(JSON.stringify(obj))
	   		}).appendTo("body").click(function() {
	   		   $(this).remove();
	   		})[0].click();
         }
         
         function fn_downloadImage(url) {
       	 	var a = $("<a>")
       	    	.attr("href", url)
       	    	.attr("download", "img.png")
       	    	.appendTo("body");

        	a[0].click();
        	a.remove();
       	 }
         
         function fn_deleteAll(){
            var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/deleteAllData.do' />");
            comSubmit.submit();
         }
         
         function fn_delete(obj){
            var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/deleteDataFromId.do' />");
            comSubmit.addParam("id", $(obj).parent().children("[data=id]").val());
            comSubmit.submit();
         }
         
         function fn_downloadAllJsonTree(){
             var comSubmit = new ComSubmit();
             comSubmit.setUrl("<c:url value='/downloadAllJsonTree.do' />");
             comSubmit.submit();
         }
         
         function fn_downloadAllZipJsonTxt(){
             var comSubmit = new ComSubmit();
             comSubmit.setUrl("<c:url value='/downloadAllZipJsonTxt.do' />");
             comSubmit.submit();
         }
         
 		$(document).ready(function(){
        	 
 			var totalData = ${totalCount};     // 총 데이터 수
            var dataPerPage = 7;              // 한 페이지에 나타낼 데이터 수
            var pageCount = 5;                 // 한 화면에 나타낼 페이지 수
 			var currentPage = ${currentPage}; // 현재 페이지
 			
            if(currentPage == '') currentPage = 1;
 			
 			paging(totalData, dataPerPage, pageCount, currentPage);
        	 
       	 	$("#search").on("click", function(e){ // 검색
                e.preventDefault();
       	 		var dataType = $("#dataTypeInput").val();
       	 		var label = $("#labelInput").val()
                fn_search(dataType, label);
            });
       	 	
	       	$("#explain").on("click", function(e){ // inform.jsp 페이지 이동
	            e.preventDefault();
	   	 	    fn_goToInform();
	        });
        	 
            $("#deleteAll").on("click", function(e){ //데이터 전체 삭제
                e.preventDefault();
                fn_deleteAll();
            });
             
            $("a[data='delete']").on("click", function(e){ //데이터 선택 삭제
                e.preventDefault();
                fn_delete($(this));
            });
            
            $("#downloadAllImage").on("click", function(e){ //이미지 전체 다운로드
                e.preventDefault();
                fn_downloadAllZipImage();
            });
            
            $("a[data='downloadImage']").on("click", function(e){ //이미지 선택 다운로드
                e.preventDefault();
                var src = $(this).parent().parent().children("[data=img]").children().attr('src');
                fn_downloadImage(src);
            });
            
            $("a[data='downloadSensor']").on("click", function(e){ //센서값 선택 다운로드
                e.preventDefault();
            
                var idx = $(this).parent().parent().children("[data=sensorId]").text();
                var dataType = $(this).parent().parent().children("[data=dataType]").text();
                var label = $(this).parent().parent().children("[data=label]").text();
                var extraData = $(this).parent().parent().children("[data=extraData]").text();
                var registerDate = $(this).parent().parent().children("[data=registerDate]").text();
                
                var SensorValue = {idx, dataType, label, extraData, registerDate};
                
                fn_downloadJsonTxt(SensorValue);
            });
            
            $("#downloadAllSensor").on("click", function(e){ //센서값 전체 다운로드
            	
            	e.preventDefault();
            	fn_downloadAllJsonTree();
            	
            });
            
        });
         
    </script>
</body>
</html>
