from django.test import TestCase
from django.core.urlresolvers import reverse


class TeamTest(TestCase):
    def test_view_team(self):
        """
        A user should be able to access the team page
        """
        response = self.client.get(reverse('team'))
        self.assertEqual(response.status_code, 200)
        self.assertContains(response, "Team")
