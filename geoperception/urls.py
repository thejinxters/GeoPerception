from django.conf.urls import patterns, include, url
from django.contrib import admin
from geoperception.views import HomeView, AboutView, TeamView, HeatmapView, HashtagView, Ajax

urlpatterns = patterns('',

    # Static Views
    url(r'^$', HomeView.as_view(), name='home'),
    url(r'^about/$', AboutView.as_view(), name='about'),
    url(r'^team/$', TeamView.as_view(), name='team'),
    url(r'^heatmap/$', HeatmapView.as_view(), name='heatmap'),

    # Imported Views
    url(r'^admin/', include(admin.site.urls)),

    # Ajax Calls
    url(r'^ajax/tweets/', Ajax.get_tweet_data, name='ajax-tweets'),

)
