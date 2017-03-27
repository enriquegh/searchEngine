    <form action="$action$" method="post">
        <table border="0">
            <tr>
                <td>Usename:</td>
                <td><input type="text" name="user" size="30"></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="pass" size="30"></td>
            </tr>
        </table>

        <p><input type="submit" value="$actionValue$"></p>
    </form>
    $if(registerLink)$
    <p>(<a href="/register">new user? register here.</a>)</p>
    $endif$