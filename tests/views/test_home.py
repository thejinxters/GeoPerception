from django.test import TestCase
from django.core.urlresolvers import reverse


class HomeTest(TestCase):
    def test_view_homepage(self):
        """
        A user should be able to access the homepage
        """
        # response = self.client.get(reverse('home'))
        # self.assertEqual(response.status_code, 200)
        # self.assertContains(response, "GeoPerception")
