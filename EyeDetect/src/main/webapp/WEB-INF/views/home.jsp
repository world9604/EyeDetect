<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
<%@ include file="/WEB-INF/include/include-header.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<h1>
	Hello world!  
</h1>
<a href="#this" class="btn" id="downloadAllImage">이미지 전체 다운로드</a>
<a href="#this" class="btn" id="downloadAllSensor">센서값 전체 다운로드</a>
<a href="#this" class="btn" id="deleteAll">데이터 전체 삭제</a>
<div style="float:right;">
	<input type="text" placeholder="아직 구현 안됬어용ㅠ">
	<a href="#this" class="btn" id="">검색</a>
</div>
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
            <th scope="col">label</th>
            <th scope="col">roll</th>
            <th scope="col">pitch</th>
            <th scope="col">yaw</th>
            <th scope="col">br</th>
            <th scope="col">register date</th>
            <th scope="col">btn</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${fn:length(sensors) > 0}">
                <c:forEach items="${sensors}" var="sensor">
                    <tr data="sensor">
                    	<td data="sensorId">${sensor.id}</td>
                     	<td data="img">
                     		<img data="${sensor.id}" src='data:image/png;base64,${sensor.encodedImage}' alt="image" />
                   		</td>
                        <td data="label">${sensor.label}</td>
                        <td data="roll">${sensor.roll}</td>
                        <td data="pitch">${sensor.pitch}</td>
                        <td data="yaw">${sensor.yaw}</td>
                        <td data="br">${sensor.br}</td>
						<td data="registerDate">${sensor.registerDate}</td>
						<td>
							<input type="hidden" data="id" value="${sensor.id}">
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
         $(document).ready(function(){
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
            
                var sensorId = $(this).parent().parent().children("[data=sensorId]").text();
                var label = $(this).parent().parent().children("[data=label]").text();
                var roll = $(this).parent().parent().children("[data=roll]").text();
                var pitch = $(this).parent().parent().children("[data=pitch]").text();
                var yaw = $(this).parent().parent().children("[data=yaw]").text();
                var br = $(this).parent().parent().children("[data=br]").text();
                var registerDate = $(this).parent().parent().children("[data=registerDate]").text();
                
                var SensorValue = {sensorId, label, roll, pitch, yaw, br, registerDate};
                
                fn_downloadJsonTxt(SensorValue);
            });
            
            $("#downloadAllSensor").on("click", function(e){ //센서값 전체 다운로드
            	
            	e.preventDefault();
            	fn_downloadAllJsonTree();
            	
            });
            
        });
         
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
            comSubmit.addParam("id", $(obj).prev().prev().prev().val());
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
         
    </script>
</body>
</html>
