FROM postgres:17

ENV POSTGRES_DB=eventpro_db
ENV POSTGRES_USER=eventpro_user
ENV POSTGRES_PASSWORD=eventpro_password

EXPOSE 5432