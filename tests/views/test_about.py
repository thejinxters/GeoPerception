from django.test import TestCase
from django.core.urlresolvers import reverse


class AboutTest(TestCase):
    def test_view_about(self):
        """
        A user should be able to access the about page
        """
        response = self.client.get(reverse('about'))
        self.assertEqual(response.status_code, 200)
        self.assertContains(response, "About")
