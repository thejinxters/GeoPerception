from django.views.generic import TemplateView


class GenericView(TemplateView):
    template_name = None

    def get(self, request, *args, **kwargs):
        context = {
            'variable_from_homeview': 'This comes from Geoperception/views.py'
        }
        return self.render_to_response(context)

class HeatmapView(GenericView):
    template_name = 'heatmap.html'

class HomeView(GenericView):
    template_name = 'home.html'

class AboutView(GenericView):
    template_name = 'about.html'

class TeamView(GenericView):
    template_name = 'team.html'
