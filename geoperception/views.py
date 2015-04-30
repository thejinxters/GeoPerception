from django.views.generic import TemplateView
from django.http import JsonResponse
from geoperception.models import Tweets, TopHashtags
from django.db import connections


class GenericView(TemplateView):
    template_name = None

    def get_context_data(self, **kwargs):
        context = super(TemplateView, self).get_context_data(**kwargs)
        context['tweets'] = Tweets.objects.all()
        context['top_hashtags'] = TopHashtags.objects.all()
        return context


class HeatmapView(GenericView):
    template_name = 'heatmap.html'


class HomeView(GenericView):
    template_name = 'home.html'


class AboutView(GenericView):
    template_name = 'about.html'


class TeamView(GenericView):
    template_name = 'team.html'


class HashtagView(GenericView):
    template_name = 'hashtags.html'


# Ajax Functions
class Ajax:
    def get_tweet_data(request):
        tweet_id_list = request.POST.getlist('tweetIds[]')
        tweet_query = str(tweet_id_list).replace("'", "").strip('[]')
        try:
            cursor = connections['cassandra'].cursor()
            result = cursor.execute(
                "SELECT * from tweets WHERE id IN (" + tweet_query + ")"
                )
            return JsonResponse(dict(response=list(result)))
        except:
            return JsonResponse({})

    def get_tweet_ids(request):
        hastag_list = request.POST.getlist('hashtags[]')
        tweet_query = str(hastag_list).strip('[]')
        try:
            cursor = connections['cassandra'].cursor()
            result = cursor.execute(
                "SELECT * from hashtag_tweets WHERE hashtag IN (" + tweet_query + ")"
                )
            result_list = []
            for res in result:
                tweet_ids = res['tweetids']
                for tweet in tweet_ids:
                    result_list.append(str(tweet))
            return JsonResponse(dict(response=result_list))

        except:
            return JsonResponse({})
