from django.views.generic import TemplateView


class Home(TemplateView):
    template_name = 'home.html'

    def get(self, request, *args, **kwargs):
        context = {
          'variable_from_homeview': 'This comes from Geoperception/views.py'
        }
        return self.render_to_response(context)
