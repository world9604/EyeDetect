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

<br/>

<div style="float:right;">
   	<a href="#this" class="btn" id="listData">list data</a>
</div>

<br/>
<br/>

<table class="board_list">
    <colgroup>
    	<col width="10%"/>
        <col width="10%"/>
    </colgroup>
    <thead>
        <tr>	
        	<th scope="col">dataType</th>
            <th scope="col">label</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${fn:length(sensors) > 0}">
                <c:forEach items="${sensors}" var="sensor">
                    <tr data="sensor">
                    	<td data="dataType">${sensor.dataType}</td>
                        <td data="label">${sensor.label}</td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="2">조회된 결과가 없습니다.</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

<br/>
<br/>
<br/>

<table class="board_list">
	<thead>
		<tr><h2>Query String Parameters</h2></tr>
	</thead>
	<tr>
		<td>Name</td>
		<td>Description</td>
		<td>Examples</td>
	</tr>
	<tr>
		<td>dataType</td>
		<td>찾고자 하는 이미지 타입을 명시</td>
		<td>
			<p>http://localhost:8080/eyedetect/getJson.do?dataType=bitmap_both</p>
			<p>http://localhost:8080/eyedetect/getJson.do?dataType=bitmap_both&label=NONE</p>
		</td>
	</tr>
	<tr>
		<td>label</td>
		<td>찾고자 하는 라벨 명시 (등록 되지 않은 라벨은 NONE 로 정의)</td>
		<td>
			<p>http://localhost:8080/eyedetect/getJson.do?label=NONE</p>
			<p>http://localhost:8080/eyedetect/getJson.do?dataType=bitmap_both&label=NONE</p>
		</td>
	</tr>
</table>

<br/>
<br/>
<br/>

<table class="board_list">
	<thead>
		<tr><h2>Return Type</h2></tr>
	</thead>
	<tr>
		<td>Name</td>
		<td>Description</td>
		<td>Examples</td>
	</tr>
	<tr>
		<td>&#60;format&#62;</td>
		<td>json</td>
		<td></td>
	</tr>
	<tr>
		<td>idx</td>
		<td>데이터의 고유한 번호</td>
		<td>
			"260"
		</td>
	</tr>
	<tr>
		<td>imageByteArray</td>
		<td>해당 이미지에 byte[]</td>
		<td>
			[12, 23, 43, ...]	
		</td>
	</tr>
	<tr>
		<td>dataType</td>
		<td>해당 이미지에 타입 종류(눈인식_안드로이드:bitmap_..., 치매_안드로이드:iris)</td>
		<td>
			bitmap_both, bitmap_right, bitmap_left, iris
		</td>
	</tr>
	<tr>
		<td>label</td>
		<td>해당 이미지에 라벨</td>
		<td>
			"김태인" (등록 되지 않은 라벨은 "NONE" 로 정의)
		</td>
	</tr>
	<tr>
		<td>extraDataJsonObject</td>
		<td>어플리케이션 마다에 추가적인 데이터 값</td>
		<td>
			json format
		</td>
	</tr>
	<tr>
		<td>roll</td>
		<td>자이로스코프의 x성분</td>
		<td>
			"85.1"
		</td>
	</tr>
	<tr>
		<td>pitch</td>
		<td>자이로스코프의 y성분</td>
		<td>
			"-14.8"
		</td>
	</tr>
	<tr>
		<td>yaw</td>
		<td>자이로스코프의 z성분</td>
		<td>
			"-8.4"
		</td>
	</tr>
	<tr>
		<td>br</td>
		<td>조도값</td>
		<td>
			"60.0"
		</td>
	</tr>
	<tr>
		<td>registerDate</td>
		<td>해당 이미지 촬영 날짜</td>
		<td>
			"2018-09-05"
		</td>
	</tr>
</table>
     
<br/>
<br/>
<br/>
     


    <%@ include file="/WEB-INF/include/include-body.jsp" %>
    <script type="text/javascript">
         $(document).ready(function(){
        	 
        	 $("#listData").on("click", function(e){ // listData.jsp 페이지 이동
 	            e.preventDefault();
 	   	 	    fn_goToListData();
 	        });
        	 
        });
         
         function fn_goToListData(dataType, label){
          	var comSubmit = new ComSubmit();
              comSubmit.setUrl("<c:url value='/fn_goToListData.do' />");
              comSubmit.submit();
           }
    </script>
</body>
</html>
