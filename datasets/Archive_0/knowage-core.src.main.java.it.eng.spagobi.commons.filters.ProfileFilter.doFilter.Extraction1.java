@Override public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException {
  try {
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest=(HttpServletRequest)request;
      HttpServletResponse httpResponse=(HttpServletResponse)response;
      HttpSession session=httpRequest.getSession();
      RequestContainer requestContainer=(RequestContainer)session.getAttribute(Constants.REQUEST_CONTAINER);
      if (requestContainer == null) {
        requestContainer=new RequestContainer();
        SessionContainer sessionContainer=new SessionContainer(true);
        requestContainer.setSessionContainer(sessionContainer);
        session.setAttribute(Constants.REQUEST_CONTAINER,requestContainer);
      }
      ResponseContainer responseContainer=(ResponseContainer)session.getAttribute(Constants.RESPONSE_CONTAINER);
      if (responseContainer == null) {
        responseContainer=new ResponseContainer();
        SourceBean serviceResponse=new SourceBean(Constants.SERVICE_RESPONSE);
        responseContainer.setServiceResponse(serviceResponse);
        session.setAttribute(Constants.RESPONSE_CONTAINER,responseContainer);
      }
      SessionContainer sessionContainer=requestContainer.getSessionContainer();
      SessionContainer permanentSession=sessionContainer.getPermanentContainer();
      IEngUserProfile profile=(IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
      UserProfile publicProfile=PublicProfile.evaluatePublicCase(httpRequest,session,permanentSession);
      if (publicProfile != null)       profile=publicProfile;
      if (profile == null) {
        logger.debug("User profile not found in session, creating a new one and putting in session....");
        String userId=null;
        if (ChannelUtilities.isWebRunning() && !GeneralUtilities.isSSOEnabled()) {
          try {
            userId=getUserIdInWebModeWithoutSSO(httpRequest);
          }
 catch (          Exception e) {
            logger.error("Error authenticating user",e);
            httpRequest.getRequestDispatcher("/WEB-INF/jsp/commons/silentLoginFailed.jsp").forward(request,response);
            return;
          }
        }
 else {
          userId=getUserIdWithSSO(httpRequest);
        }
        logger.debug("User id = " + userId);
        if (userId != null && !userId.trim().equals("")) {
          profile=GeneralUtilities.createNewUserProfile(userId);
          if (requestIsForHomePage(httpRequest)) {
            profile=SessionUserProfileBuilder.getDefaultUserProfile((UserProfile)profile);
          }
          storeProfileInSession((UserProfile)profile,permanentSession,session);
        }
 else {
          logger.debug("User identifier not found.");
        }
      }
 else {
      }
      doFilter_extraction_4(request,response,chain,httpRequest,httpResponse,profile);
    }
  }
 catch (  Exception e) {
    logger.error("Error while service execution",e);
    ((HttpServletResponse)response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    return;
  }
 finally {
    TenantManager.unset();
    UserProfileManager.unset();
  }
}
