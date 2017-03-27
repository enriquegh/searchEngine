<html>
    <head><title>$title$</title></head>
    <body background="http://cs.usfca.edu/usf-in-pictures/IMG_3515.jpg" link="white" vlink="#A5AAB6">
        <p><b>Login:</b><a href="/welcome" style="color: white">Here</a></p>
        <center>

            <h1> ChaChing </h1>
            <form method="post" action=$requestPath$>
            <input type="text" name="searchBar" maxlength="50" size="20">
            </p><input type="submit" value="Search"></p>
            </form>

            $results :{result |<p><a href= "$result.path$">$result.path$</a></p>$\n$}$
            $if(!firstTime && isSizeZero)$
            <p>Yikes! Looks like there are no results for your query.</p>
            $endif$
        </center>
    </body>
</html>