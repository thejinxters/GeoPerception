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
        "fields": ["hashtag", "tweetids"]
    };
    $.ajax({
        url: hostname + "hashtag_tweets/_search",
        type: "POST",
        dataType: "JSON",
        data: JSON.stringify(postData),
        success: function(data) {
            var response = $.map(data.hits.hits, function(item) {
                return {
                    hashtag: item.fields.hashtag,
                    tweets: item.fields.tweetids
                }
            });
            if(response.length){
                var tweetlist = [];
                $(response).each(function (){
                   $(this.tweets[0].split(',')).each( function() {
                        tweetlist.push(this.toString());
                    });
                    tweetlist = removeDuplicatesFromArray(tweetlist);
                    // var display = '<ul>'
                    // $(tweetlist).each( function() {
                    //     display += '<li>'+this+'</li>'
                    // });
                    // display += '</ul>'
                    // $('#tweetlist-display').html(display);
                });
                getTweetsFromCassandra(tweetlist);
            }
            else{
                $('#tweetlist-display').html("There were no results that matched your query");
            }
        },
    });
}

// Get tweets for mapping
function getTweetsFromCassandra(tweetlist){
    var postData = {
        'tweetIds' : tweetlist
    };
    console.log(postData);
    $.ajax({
        url: "/ajax/tweets/",
        type: "GET",
        dataType: "JSON",
        data: postData,
        success: function(data) {
            console.log(data);
            // remove all tweets
            clearMarkers();
            // Add tweets to map
            tweets = data.response;
            $(data.response).each(function(){
                addTweetToMap(this);
            });
            fitAllMarkersInView();
        }
    });

}



function removeDuplicatesFromArray(array){
    var uniqueNames = [];
    $.each(array, function(i, el){
        if($.inArray(el, uniqueNames) === -1) uniqueNames.push(el);
    });
    return uniqueNames;
}
