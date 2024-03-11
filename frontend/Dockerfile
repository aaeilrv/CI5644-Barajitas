FROM node:20-alpine

EXPOSE 3000
COPY '.' '.'
RUN apk --no-cache add curl
RUN npm install
RUN npm run build

ENTRYPOINT ["npm", "run", "start"]