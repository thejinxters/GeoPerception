var usa = new google.maps.LatLng(38.8833, 265.9833);

var markers = [];
var map;
var image = "{% static 'images/tweet-30.png' %}";

// Initialize Map
function initialize() {
  var mapOptions = {
    zoom: 4,
    center: usa
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
          mapOptions);
  addCassandraTweets();
}

// When Drop is clicked, set up large amount of inserts
function drop() {
  for (var i = 0; i < 1000; i++) {
    insertMarker(i);
  }
}

// Abstract setTimeout into its own function
function insertMarker(i){
  window.setTimeout(function() {
      addMarker();
    }, 100000 * Math.random());
}

// Adds marker to the marker list
function addMarker() {
  markers.push(new google.maps.Marker({
    position: new google.maps.LatLng((Math.random() - 0.5) * 180, (Math.random() - 0.5) * 360),
    map: map,
    icon: image,
    title: 'tweet'
  }));
}

function addCassandraTweets() {
  var tweetMarkers = [];
  var tweetContent = [];
}

// When clear is clicked, remove all markers
function clearMarkers() {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
  markers = [];
}

google.maps.event.addDomListener(window, 'load', initialize);
