<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<title>6998 TwittMap Group14</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

<style>
@import url(http://fonts.googleapis.com/css?family=Montserrat);

* {
	margin: 0;
	padding: 0;
}

.nav {
	background: #232323;
	height: 60px;
	width: 100%;
	display: inline-block;
}

.nav li {
	float: left;
	list-style-type: none;
	position: relative;
}

.nav li a {
	font-size: 16px;
	color: white;
	display: block;
	line-height: 60px;
	padding: 0 26px;
	text-decoration: none;
	border-left: 1px solid #2e2e2e;
	font-family: Montserrat, sans-serif;
	text-shadow: 0 0 1px rgba(255, 255, 255, 0.5);
}

.nav li a:hover {
	background-color: #2e2e2e;
}

#settings a {
	padding: 18px;
	height: 24px;
	font-size: 10px;
	line-height: 24px;
}

#search {
	width: 357px;
	margin: 4px;
}

#search_text {
	width: 297px;
	padding: 15px 0 15px 20px;
	font-size: 16px;
	font-family: Montserrat, sans-serif;
	border: 0 none;
	height: 52px;
	margin-right: 0;
	color: white;
	outline: none;
	background: #9b9b9b;
	float: left;
	box-sizing: border-box;
	transition: all 0.15s;
}

::-webkit-input-placeholder { /* WebKit browsers */
	color: white;
}

:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
	color: white;
}

::-moz-placeholder { /* Mozilla Firefox 19+ */
	color: white;
}

:-ms-input-placeholder { /* Internet Explorer 10+ */
	color: white;
}

#search_text:focus {
	background: rgb(128, 128, 128);
}

#search_button {
	border: 0 none;
	/*background: #9b9b9b url('search.png') no-repeat no-repeat;*/
	width: 60px;
	float: left;
	padding: 0;
	text-align: center;
	height: 52px;
	cursor: pointer;
}

#options a {
	border-left: 0 none;
}

#options>a {
	/*   background-image: url(triangle.png);*/
	background-position: 85% center;
	background-repeat: no-repeat;
	padding-right: 42px;
}

.subnav {
	visibility: hidden;
	position: relative;
	z-index: 9999;
	top: 110%;
	right: 0;
	width: 200px;
	height: auto;
	opacity: 0;
	transition: all 0.1s;
	background: #232323;
}

.subnav li {
	float: none;
}

.subnav li a {
	border-bottom: 1px solid #2e2e2e;
}

#options:hover .subnav {
	visibility: visible;
	top: 100%;
	opacity: 1;
}

html, body {
	height: 100%;
	margin: 0;
	padding: 0;
}

#map {
	height: 100%;
}
</style>
</head>

<body
	](http://msdn.microsoft.com/en-au/library/ie/ms535874%28v=vs.85%29.aspx](http://socket.io />

<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/socket.io/1.3.6/socket.io.min.js"></script>
<ul class="nav">

	<li><a href="#">Group 14 TwittMap</a></li>

	<li id="search">
		<form action="" method="get">
			<input type="text" name="" id="search_text" placeholder="Search" />
			<input type="button" name="search_button" id="search_button">
			</button>
		</form>
	</li>
	<li id="options"><a href="#">Keywords</a>
		<ul class="subnav">
			<li><a onclick="searchKeyword('Love')">Love</a></li>
			<li><a onclick="searchKeyword('job')">job</a></li>
			<li><a onclick="searchKeyword('nyc')">nyc</a></li>
			<li><a onclick="searchKeyword('light')">light</a></li>
			<li><a onclick="searchKeyword('work')">work</a></li>
			<li><a onclick="searchKeyword('China')">China</a></li>
		</ul></li>
</ul>

<!-- <script src="prefixfree-1.0.7.js" type="text/javascript"></script> -->
<div id="map"></div>
<script>
	var markers = [];
	var map;

	function initMap() {
		var myLatLng = {
			lat : 42,
			lng : -73.2
		};
		var geo = '{"lat": 51.0,"lng": -70}';
		map = new google.maps.Map(document.getElementById('map'), {
			zoom : 2,
			center : myLatLng
		});
		setInterval(function() {
			getData();
		}, 5000);
	}
	
	var keyWord;
	//send keyword to backend and fetch data
	function searchKeyword(kw) {
		setMapOnAll(markers);
		keyWord = kw;
		initMap();
		$.ajax({
			url : "/trend/keyword/" + keyWord,
			type : "GET",
			dataType : 'json',

			error : function(request, status, error) {
				alert('searchKeyWord error');
			},
			success : function(data) {
				getData();
			}
		});
		console.log("successful!");
	};

	function getData() {
		$.ajax({
			url : "/trend/display/" + keyWord,
			type : "GET",
			dataType : 'json',

			error : function(request, status, error) {
				alert('searchKeyWord error');
			},
			success : function(data) {
				onmessage(JSON.stringify(data));
			}
		});
		console.log("successful!");
	};

	function onmessage(event) {

		var msg = $.parseJSON(event);
		for (var i = 0; i < msg.length; i++) {
			extracttweet(msg[i]);
		}
	}
	function extracttweet(msgi) {
		var infoWindow = new google.maps.InfoWindow();
		var icon = "";
		var text = msgi.text;
		switch (msgi.sentiment) {
		case "POSITIVE":
			icon = "green";
			break;
		case "NEUTRAL":
			icon = "yellow";
			break;
		case "NEGATIVE":
			icon = "red";
			break;
		}
		icon = "http://maps.google.com/mapfiles/ms/icons/" + icon + ".png"
		// var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
		//       new google.maps.Size(21, 34),
		//       new google.maps.Point(0,0),
		//       new google.maps.Point(10, 34));
		//   	var pinShadow = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
		//       new google.maps.Size(40, 37),
		//       new google.maps.Point(0, 0),
		//       new google.maps.Point(12, 35)); 
		var location = {
			lat : msgi.lag,
			lng : msgi.lon
		};
		var marker = new google.maps.Marker({
			// icon: pinImage,
			// shadow: pinShadow,
			position : location,
			map : map,
			icon : new google.maps.MarkerImage(icon)
		});
		(function(marker, text) {
			google.maps.event.addListener(marker, "click", function() {
				infoWindow.setContent(text);
				infoWindow.open(map, marker);
			});
		})(marker, text);
		markers.push(marker)
	}
	//clear markers
	function setMapOnAll(markers) {
		for (var i = 0; i < markers.length; i++) {
			markers[i].setMap(null);
		}
		markers = [];
	}

	//search button
	$('#search_button').on('click', function() {
		var keyWord = $('#search_text').val();
		searchKeyword(keyWord);
	});
</script>
<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDyw28-bFdXq92O8OY0X1SB7msnLA7-nCo&signed_in=true&callback=initMap"></script>
</body>
</html>