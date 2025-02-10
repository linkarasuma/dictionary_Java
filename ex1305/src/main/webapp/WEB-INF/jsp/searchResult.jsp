<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>検索結果</title>
<style>
@import url('https://fonts.googleapis.com/css?family=Sawarabi+Mincho');
body {
	font-family: 'Sawarabi Mincho', sans-serif;
    letter-spacing: 1px;
}
.center {
	text-align: center;
}
.wrapper {
    font-size: 13px;
    width: 700px;
    margin: 10px auto 0;
}
h1 {
	background-color: #f7f7f7;
    padding: 10px;
    margin: 0 0 20px;
    border-bottom: 6px double #000;
    border-top: 6px double #000;
    letter-spacing: 4px;
}
ul,li {
	list-style: none;
}
ul {
	padding: 20px 0 0 0;
	margin: 20px 0px 0px 0px;
	border-top: 1px solid #d3d3d3;
}
.word_title {
	background-color: #f7f7f7;
    padding: 10px 12px;
    margin: 0 0 10px;
    font-weight: bold;
    border-left: 6px double #000;
}
.word_description {
    padding: 0px 20px 10px 40px;
    margin: 10px 0px;
}
a,a:visited {
    text-decoration: none;
    color: #000;
}
a,a:visited {
    border: 2px solid #000;
    padding: 6px 0px;
    font-weight: bold;
    display: block;
    width: 120px;
    margin: 0 auto;
}
a:hover {
	opacity: 0.5;
}
</style>
</head>
<body>
<div class="wrapper">
<h1 class="center">英和辞書</h1>

<!-- 検索フォーム -->
<form action="SearchResult" method="get">
    <label for="word">検索する英単語：</label>
    <input type="text" id="word" name="word" value="${param.word}" required>
    <label for="limit">　　最大表示件数：</label>
    <input type="number" id="limit" name="limit" min="1" value="${param.limit}">
    <input type="submit" value="検索">
</form>


<c:if test="${not empty wordList}">
    <ul>
    <c:forEach var="word" items="${wordList}">
        <li><p class="word_title">${word.word}</p><p class="word_description">${word.description.replace("/", "<br>")}</p></li>
    </c:forEach>
    </ul>
</c:if>
<c:if test="${empty wordList}">
    <p>該当する単語が見つかりませんでした。</p>
</c:if>
<a class="center" href="search.jsp">> 戻る</a>
</div>
</body>
</html>
