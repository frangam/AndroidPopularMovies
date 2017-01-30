# Android Popular Movies
Simple &amp; Educational Android App for showing popular movies (Associate Android Developer Fast Track at Udacity: https://www.udacity.com/course/associate-android-developer-fast-track--nd818)

# themoviedb.org API
This project use the themoviedb.org API. So, if you want to run it you must create an account at themoviedb.org site. In your request for a key, state that your usage will be for educational/non-commercial use. You will also need to provide some personal information to complete the request. Once you submit your request, you should receive your key via email shortly after.
  - In order to request popular movies you will want to request data from the /movie/popular and /movie/top_rated endpoints. An API Key is required.
  - Once you obtain your key, you append it to your HTTP request as a URL parameter like so:
    - http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
You will extract the movie id from this request. You will need this in subsequent requests.

# Version
1.0.0
