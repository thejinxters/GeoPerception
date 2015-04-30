hostname = "http://52.6.138.104:9200/";

// AutoComplete for Search Field
$(function() {
    $("#hashtag-search").autocomplete({
        source: function(request, response) {
            var wildcard = { "hashtag": "*"+request.term.toLowerCase()+"*" };
            var postData = {
                "query": { "wildcard": wildcard },
                "fields": ["hashtag", "tweetids", "_id"]
            };
            $.ajax({
                url: hostname + "hashtag_tweets/_search",
                type: "POST",
                dataType: "JSON",
                data: JSON.stringify(postData),
                success: function(data) {
                    response($.map(data.hits.hits, function(item) {
                        return {
                            label: item.fields.hashtag,
                            id: item.fields._id
                        }
                    }));
                },
            });
        },
        minLength: 2
    })
});

// Get Search Results
function elasticSearch(){
    request = $('#hashtag-search').val();
    var match = { "hashtag": request };
    var postData = {
        "query": { "match": match },
        "fields": ["hashtag"]
    };
    $.ajax({
        url: hostname + "hashtag_tweets/_search",
        type: "POST",
        dataType: "JSON",
        data: JSON.stringify(postData),
        success: function(data) {
            var response = $.map(data.hits.hits, function(item) {
                return {
                    hashtag: item.fields.hashtag
                }
            });
            if(response.length){
                var hashtaglist = [];
                $(response).each(function (){
                   $(this.hashtag[0].split(',')).each( function() {
                        hashtaglist.push(this.toString());
                    });
                });
                getTweetIdsFromCassandra(hashtaglist);
            }
            else{
                $('#tweetlist-display').html("There were no results that matched your query");
            }
        },
    });
}

function getTweetIdsFromCassandra(hashtaglist){
    var postData = {
        'hashtags' : hashtaglist
    };
    $.ajax({
        url: "/ajax/tweetids/",
        type: "POST",
        dataType: "JSON",
        data: postData,
        success: function(data) {
            var tweetlist = data.response;
            getTweetsFromCassandra(tweetlist);
        }
    });
}

// Get tweets for mapping
function getTweetsFromCassandra(tweetlist){
    var postData = {
        'tweetIds' : tweetlist
    };
    $.ajax({
        url: "/ajax/tweets/",
        type: "POST",
        dataType: "JSON",
        data: postData,
        success: function(data) {
            // remove all tweets
            clearMarkers();
            // Add tweets to map
            tweets = data.response;
            $(tweets).each(function(){
                addTweetToMap(this);
            });
            fitAllMarkersInView();
            addHashtagsToSidebar(tweets)
        }
    });
}


function addHashtagsToSidebar(tweets){
    hashtagCollection = {}
    $(tweets).each( function() {
        var hashtags = this.hashtags;
        $(hashtags).each( function() {
            var hashtag = this.toString();
            if (hashtagCollection[hashtag]){
                hashtagCollection[hashtag] = hashtagCollection[hashtag] + 1;
            }
            else{
                hashtagCollection[hashtag] = 1;
            }
        });
    });

    var tuples = [];
    for (var key in hashtagCollection) tuples.push([key, hashtagCollection[key]]);
    tuples.sort(function(a, b) {
        a = a[1];
        b = b[1];
        return a > b ? -1 : (a < b ? 1 : 0);
    });

    var display = '<ul class="hashtags">'
    for (var i = 0; i < tuples.length; i++) {
        var key = tuples[i][0];
        var value = tuples[i][1];
        // console.log("key:"+key+" value:"+value);

        display += '<li><a class="item" ';
        display += 'onclick="hashtagClick(\''+key+'\')">'
        display += key + ' ('+ value + ')';
        display += '</a></li>';
    }
    display += '</ul>';
    $('.related-hashtags').html(display);
}


function hashtagClick(hashtag){
    $('#hashtag-search').val(hashtag);
    elasticSearch();
    $('html, body').animate({
        scrollTop: $("#hashtag-search").offset().top - 85
    }, 300);
}


function removeDuplicatesFromArray(array){
    var uniqueNames = [];
    $.each(array, function(i, el){
        if($.inArray(el, uniqueNames) === -1) uniqueNames.push(el);
    });
    return uniqueNames;
}
