<html xmlns:th="http://www.thymeleaf.org">
<body>



<div>
    <form method="POST" enctype="multipart/form-data" action="upload">
        <table>
            <tr>
                <td>File to upload:</td>
                <td><input type="file" name="file"/></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Upload"/></td>
            </tr>
        </table>
    </form>
</div>

<#if message??>
${message}
</#if>
</body>
</html>