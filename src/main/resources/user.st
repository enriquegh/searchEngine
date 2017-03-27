$start(title)$

$if(nullError)$
    <p style="color: red;">$errorMessage$</p>
$endif$

$if(newUser)$
    <p>
        Registration was successful!
        Login with your new username and password below.
    </p>
$endif$

$if(logout)$
    <p>Successfully logged out.</p>

$endif$

$form(action,actionValue)$

$end(date)$