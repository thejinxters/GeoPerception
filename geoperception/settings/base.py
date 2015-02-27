"""
Django settings for geoperception project.

For more information on this file, see
https://docs.djangoproject.com/en/1.7/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.7/ref/settings/
"""

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)

import os
from django.conf.global_settings import TEMPLATE_CONTEXT_PROCESSORS

SETTINGS_PATH = os.path.dirname(__file__)
BASE_DIR = os.path.dirname(os.path.dirname(SETTINGS_PATH))


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.7/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = 'j_*mzzuig)b%=4c)dspr==as2e^7oe6t8!a@=h%41lp%eg%im*'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

TEMPLATE_DEBUG = True

# Absolute paths for where the project and templates are stored.
TEMPLATES_PATH = os.path.join(BASE_DIR, "templates")
TEMPLATE_DIRS = (
    # ABSOLUTE_TEMPLATES_PATH,
    TEMPLATES_PATH,
)
TEMPLATE_CONTEXT_PROCESSORS += (
    'django.core.context_processors.request',
    )


# Allow Localhost for production setting testing
ALLOWED_HOSTS = ['localhost', '127.0.0.1']

# Application definition

PROJECT_APPS = [
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
]

DEVELOPMENT_APPS = [
    'debug_toolbar',
]

INSTALLED_APPS = PROJECT_APPS + DEVELOPMENT_APPS

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.auth.middleware.SessionAuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

ROOT_URLCONF = 'geoperception.urls'

WSGI_APPLICATION = 'geoperception.wsgi.application'


# Database
# https://docs.djangoproject.com/en/1.7/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
    }
}

# Internationalization
# https://docs.djangoproject.com/en/1.7/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_L10N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.7/howto/static-files/

STATIC_URL = '/static/'
STATICFILES_DIRS = [os.path.join(BASE_DIR, 'static'), os.path.join(BASE_DIR, 'vendor'),]
