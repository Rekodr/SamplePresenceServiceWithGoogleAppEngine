    package restapi;

    import java.util.List;

    import com.googlecode.objectify.Key;
    import com.googlecode.objectify.Objectify;
    import org.json.JSONArray;
    import org.restlet.data.Form;
    import org.restlet.data.MediaType;
    import org.restlet.data.Status;
    import org.restlet.ext.json.JsonRepresentation;
    import org.restlet.representation.Representation;
    import org.restlet.representation.StringRepresentation;
    import org.restlet.representation.Variant;
    import org.restlet.resource.ResourceException;
    import org.restlet.resource.ServerResource;
    import org.restlet.resource.Post;
    import org.restlet.resource.Get;
    import com.googlecode.objectify.ObjectifyService;

    /**
     * Represents a collection of users.  This resource processes HTTP requests that come in on the URIs
     * in the form of:
     *
     * This resource supports both HTML and JSON representations.
     *
     * @author Recodeo Rekod.   Credit to: Jonathan Engelsma (http://themobilemontage.com)
     *
     */
    public class UsersResource extends ServerResource {

        private List<User> users = null;

        @SuppressWarnings("unchecked")
        @Override
        public void doInit() {

            this.users = ObjectifyService.ofy()
                    .load()
                    .type( User.class) // We want only Widgets
                    .list();

            // these are the representation types this resource can use to describe the
            // set of users with.
            getVariants().add(new Variant(MediaType.TEXT_HTML));
            getVariants().add(new Variant(MediaType.APPLICATION_JSON));

        }



        /**
         * Handle an HTTP GET. Represent the widget object in the requested format.
         *
         * @param variant
         * @return
         * @throws ResourceException
         */
        @Get
        public Representation get(Variant variant) throws ResourceException {
            Representation result = null;
            if (null == this.users) {
                ErrorMessage em = new ErrorMessage();
                return representError(variant, em);
            } else {

                if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {

                    JSONArray usersArray = new JSONArray();
                    for(Object o : this.users) {
                        User w = (User)o;
                        usersArray.put(w.toJSON());
                    }

                    result = new JsonRepresentation(usersArray);

                } else {

                    // create a plain text representation of our list of users
                    StringBuffer buf = new StringBuffer("<html><head><title>User Resources</title><head><body><h1>User Resources</h1>");
                    buf.append("<br/><h2> There are " + this.users.size() + " total.</h2>");
                    for(Object o : this.users) {
                        User w = (User)o;
                        buf.append(w.toHtml(true));
                    }
                    buf.append("</body></html>");
                    result = new StringRepresentation(buf.toString());
                    result.setMediaType(MediaType.TEXT_HTML);
                }
            }
            return result;
        }

        /**
         * Handle a POST Http request. Create a new user
         *
         * @param entity
         * @throws ResourceException
         */
        @Post
        public Representation post(Representation entity, Variant variant)
                throws ResourceException
        {

            Representation rep = null;

            // We handle only a form request in this example. Other types could be
            // JSON or XML.
            try {
                if (entity.getMediaType().equals(MediaType.APPLICATION_WWW_FORM, true))
                {
                    Form form = new Form(entity);
                    User usr = new User();
                    usr.setUsername(form.getFirstValue("username"));
                    usr.setPort( Integer.parseInt(form.getFirstValue( "port" ) ));
                    usr.setStatus( Boolean.parseBoolean( form.getFirstValue( "status" ) ) );
                    usr.setHost( form.getFirstValue( "host" ) );


                    // persist updated object
                    Key<User> theKey = Key.create( User.class, usr.getUsername());
                    User exisiting_usr = ObjectifyService.ofy().load().key( theKey ).now();

                    if(exisiting_usr != null) {
                        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                        rep = new JsonRepresentation( "User already exist" );
                        rep.setMediaType(MediaType.APPLICATION_JSON);
                        getResponse().setEntity(rep);
                    } else {
                        ObjectifyService.ofy().save().entity(usr).now();
                        getResponse().setStatus(Status.SUCCESS_OK);
                        rep = new JsonRepresentation( usr.toJSON() );
                        rep.setMediaType(MediaType.APPLICATION_JSON);
                        getResponse().setEntity(rep);
                    }

                } else {
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                }
            } catch (Exception e) {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            }
            return rep;
        }

        /**
         * Represent an error message in the requested format.
         *
         * @param variant
         * @param em
         * @return
         * @throws ResourceException
         */
        private Representation representError(Variant variant, ErrorMessage em)
                throws ResourceException {
            Representation result = null;
            if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
                result = new JsonRepresentation(em.toJSON());
            } else {
                result = new StringRepresentation(em.toString());
            }
            return result;
        }

        protected Representation representError(MediaType type, ErrorMessage em)
                throws ResourceException {
            Representation result = null;
            if (type.equals(MediaType.APPLICATION_JSON)) {
                result = new JsonRepresentation(em.toJSON());
            } else {
                result = new StringRepresentation(em.toString());
            }
            return result;
        }
    }