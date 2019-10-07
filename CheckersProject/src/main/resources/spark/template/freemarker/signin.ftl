<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <!-- Don't include a navigation bar -->

    <h1>Sign In</h1>
    <#if currentUser??> <!-- The user is already signed in -->
        <p> Frick, boi. you're already signed in!</p>
    <#else>
        <#include "message.ftl">
        <form action=${webpage} method="post">
            Input Username:<br/>
            <input type="text" name="name" >
            <br/>
            <br/>
            <input type="submit" value="Submit">
            <br/>
        </form>
    </#if>

    </div>

</div>
</body>

</html>
