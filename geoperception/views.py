from django.views.generic import TemplateView


class HomeView(TemplateView):
    template_name = 'home.html'

    def get(self, request, *args, **kwargs):
        context = {
          'variable_from_homeview': 'This comes from Geoperception/views.py'
        }
        return self.render_to_response(context)


class AboutView(TemplateView):
    template_name = 'about.html'

    def get(self, request, *args, **kwargs):
        context = {
          'variable_from_aboutview': 'This comes from Geoperception/views.py'
        }
        return self.render_to_response(context)


class TeamView(TemplateView):
    template_name = 'team.html'

    def get(self, request, *args, **kwargs):
        context = {
          'variable_from_teamview': 'This comes from Geoperception/views.py'
        }
        return self.render_to_response(context)
