from django.views.generic import TemplateView
# from django.http import HttpResponse
from geoperception.models import Locations

class GenericView(TemplateView):
    template_name = None

    def get_context_data(self, **kwargs):
        context = super(TemplateView, self).get_context_data(**kwargs)
        context['bob'] = 'test'
        context['locations'] = Locations.objects.all()
        return context

    # def get(self, request, *args, **kwargs):

    #     context = {
    #         'variable_from_homeview': 'This comes from Geoperception/views.py'
    #     }
    #     # context = Locations.objects.all()
    #     return self.render_to_response(context)

class HeatmapView(GenericView):
    template_name = 'heatmap.html'

class HomeView(GenericView):
    template_name = 'home.html'

class AboutView(GenericView):
    template_name = 'about.html'

class TeamView(GenericView):
    template_name = 'team.html'
