var m = [20, 50, 5, 150],
    w = 800 - m[1] - m[3],
    h = 4250 - m[0] - m[2];

var format = d3.format(",.0f");

var x = d3.scale.linear().range([0, w]),
    y = d3.scale.ordinal().rangeRoundBands([0, h], .1);

var xAxis = d3.svg.axis().scale(x).orient("top").tickSize(-h),
    yAxis = d3.svg.axis().scale(y).orient("left").tickSize(0);


d3.json("{{ STATIC_URL }}sample-data.json", function(error, data) {

  // Parse counts, and sort by count.
  data = topHashtagsObject;
  data.forEach(function(d) { d.count = +d.count; });
  data.sort(function(a, b) { return b.count - a.count; });

  // Set the scale domain.
  x.domain([0, d3.max(data, function(d){return d.count})]);
    y.domain(data.map(function(d) { return d.hashtag; }));

  var svg = d3.select(".top-hashtags-viz").append("svg")
  .data(data)
    .attr("width", w + m[1] + m[3])
    .attr("height", h + m[0] + m[2])

  .append("g")
    .attr("transform", "translate(" + m[3] + "," + m[0] + ")")


  var bar = svg.selectAll("g.bar")
      .data(data)
      .enter().append("g")
      .attr("class", "bar")
      .attr("transform", function(d) { return "translate(0," + y(d.hashtag) + ")"; });

  bar.append("rect")
      .attr("width", function(d) { return x(d.count); })
      .attr("height", y.rangeBand())
      .on("click", function(d){hashtagClick(d.hashtag)});

  bar.append("text")
      .attr("class", "count")
      .attr("x", function(d) { return x(d.count); })
      .attr("y", y.rangeBand() / 2)
      .attr("dx", 30)
      .attr("dy", ".35em")
      .attr("text-anchor", "end")
      .text(function(d) { return format(d.count); })

  svg.append("g")
      .attr("class", "x axis")
      .call(xAxis);

  svg.append("g")
      .data(data)
      .attr("class", "y axis")
      .call(yAxis)
});
