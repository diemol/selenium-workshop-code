# Used to create a development image for working with Selenium

# You can find the new timestamped tags here: https://hub.docker.com/r/gitpod/workspace-full/tags
FROM gitpod/workspace-full:2023-11-24-15-04-57

USER root

#RUN apt-get update -qqy && apt-get install -y wget curl gnupg2

# So we can install browsers and browser drivers later
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list
RUN mkdir -p /home/gitpod/selenium /var/run/supervisor /var/log/supervisor && \
  chmod -R 777 /var/run/supervisor /var/log/supervisor

ENV DEBIAN_FRONTEND=noninteractive

# Things needed by bazel and to run tests

RUN apt-get update -qqy && \
    apt-get -qy install supervisor \
                        x11vnc \
                        fluxbox \
                        xvfb && \
    rm -rf /var/lib/apt/lists/* /var/cache/apt/*

# Browsers

RUN apt-get update -qqy && \
    apt-get -qy install google-chrome-stable firefox && \
    rm -rf /var/lib/apt/lists/* /var/cache/apt/*

# noVNC exposes VNC through a web page
ENV NOVNC_TAG="1.4.0" \
    WEBSOCKIFY_TAG="0.11.0"

RUN wget -nv -O /tmp/noVNC.zip "https://github.com/novnc/noVNC/archive/refs/tags/v${NOVNC_TAG}.zip" \
  && unzip -x /tmp/noVNC.zip -d /tmp \
  && mv /tmp/noVNC-${NOVNC_TAG} /home/gitpod/selenium/noVNC \
  && cp /home/gitpod/selenium/noVNC/vnc.html /home/gitpod/selenium/noVNC/index.html \
  && rm /tmp/noVNC.zip \
  && wget -nv -O /tmp/websockify.zip "https://github.com/novnc/websockify/archive/refs/tags/v${WEBSOCKIFY_TAG}.zip" \
  && unzip -x /tmp/websockify.zip -d /tmp \
  && rm /tmp/websockify.zip \
  && mv /tmp/websockify-${WEBSOCKIFY_TAG} /home/gitpod/selenium/noVNC/utils/websockify

USER gitpod

# Supervisor
#======================================
# Add Supervisor configuration file
#======================================
COPY gitpod/supervisord.conf /etc

#==============================
# Scripts to run XVFB, VNC, and noVNC
#==============================
COPY gitpod/start-xvfb.sh \
      gitpod/start-vnc.sh \
      gitpod/start-novnc.sh \
      /home/gitpod/selenium/

# To run browser tests
ENV DISPLAY :99.0
ENV DISPLAY_NUM 99
ENV SCREEN_WIDTH 1360
ENV SCREEN_HEIGHT 1020
ENV SCREEN_DEPTH 24
ENV SCREEN_DPI 96
ENV VNC_PORT 5900
ENV NO_VNC_PORT 7900
