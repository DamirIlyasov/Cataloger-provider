<html xmlns:th="http://www.thymeleaf.org">
<body>



<div>
    <form method="POST" enctype="multipart/form-data" action="delete">
        <table>
            <tr>
                <td>File to delete:</td>
                <td><input type="file" name="file"/></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Delete"/></td>
            </tr>
        </table>
    </form>
</div>

<#if message??>
${message}
</#if>
</body>
</html>