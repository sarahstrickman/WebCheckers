<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl">

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl">

    <#assign oopsie="hi">
    <#if currentUser??>
      <div >
      <#list onlinePlayers as player>
        <#if currentUser.name == player>
          <#continue>
        </#if>
        <form action=${webpage} method="post">

        </form>
        <p> <a href="/game?self=${currentUser}&opp=${player}">${player} </a>
      </#list>

      </div>

    <#else>
      <#if numOnline??>
        <div>
          Current number of players online: ${numOnline}
        </div>
      <#else>
          <p> aw jeez</p>
      </#if>
    </#if>
  </div>

</div>
</body>

</html>
