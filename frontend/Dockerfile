FROM node:20-alpine

WORKDIR /app

# Kopiujemy pliki z listą bibliotek
COPY package*.json ./

# Instalujemy WSZYSTKO (łącznie z CLI)
RUN npm install

# Instalujemy Angular CLI globalnie, żeby komenda 'ng' zawsze działała
RUN npm install -g @angular/cli

# Kopiujemy resztę kodu
COPY . .

# Wystawiamy port
EXPOSE 4200

# ODPALAMY: host 0.0.0.0 jest obowiązkowy, żebyś widział to na Windowsie!
CMD ["ng", "serve", "--host", "0.0.0.0"]