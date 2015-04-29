var usa = new google.maps.LatLng(38.8833, 265.9833);

var markers = [];
var map;
var infowindow;

// Initialize Map
function initialize() {
  var mapOptions = {
    zoom: 4,
    center: usa
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
          mapOptions);
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

function addTweetToMap(tweet) {
  var marker = new google.maps.Marker({
    position: new google.maps.LatLng(tweet.lat, tweet.lng),
    map: map,
    icon: image,
    title: 'tweet'
  });
  google.maps.event.addListener(marker, 'click', function() {
    closeOpenTweets();
    infowindow = new google.maps.InfoWindow({
      content: '<div><h4>TWEET from' + tweet.username + ':</h4></div>' +
                '<div class="tweet-content">'+
                tweet.content +
                '</div>'
    })
    infowindow.open(map,this);
    linkifyTweet();
  });
  markers.push(marker);
}

function closeOpenTweets(){
  if(infowindow){
    infowindow.close();
  }
}

function linkifyTweet(){
  window.setTimeout(function() {
      $('.tweet-content').linkify();;
    }, 300);
}

// When clear is clicked, remove all markers
function clearMarkers() {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
  markers = [];
}

function fitAllMarkersInView(){
  if (markers.length < 30){
    var bounds = new google.maps.LatLngBounds();
    for(i=0;i<markers.length;i++) {
     bounds.extend(markers[i].getPosition());
    }

    map.fitBounds(bounds);
    map.setZoom(4)
  }
}

google.maps.event.addDomListener(window, 'load', initialize);
