FROM node:11.10.1-alpine
WORKDIR /var/www/
COPY ./package.json ./
RUN npm install
COPY . .
CMD [ "yarn", "start" ]