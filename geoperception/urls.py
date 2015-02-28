from django.conf import settings
from django.conf.urls import patterns, include, url
from django.contrib import admin
from geoperception.views import HomeView, AboutView, TeamView

urlpatterns = patterns('',
    # Static Views
    url(r'^$', HomeView.as_view(), name='home'),
    url(r'^about/$', AboutView.as_view(), name='about'),
    url(r'^team/$', TeamView.as_view(), name='team'),

    #Imported Views
    url(r'^admin/', include(admin.site.urls)),
)
